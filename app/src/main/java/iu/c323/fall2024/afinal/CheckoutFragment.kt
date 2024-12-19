package iu.c323.fall2024.afinal

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import iu.c323.fall2024.afinal.databinding.FragmentCheckoutBinding
import iu.c323.fall2024.afinal.model.MenuItemWithQuantity
import iu.c323.fall2024.afinal.model.Order
import iu.c323.fall2024.afinal.model.OrderItem
import iu.c323.fall2024.afinal.model.Restaurant
import java.util.Calendar

class CheckoutFragment : Fragment() {

    private lateinit var binding: FragmentCheckoutBinding
    private lateinit var orderItemAdapter: OrderItemAdapter
    private var orderItems: MutableList<MenuItemWithQuantity> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCheckoutBinding.inflate(inflater, container, false)

        // Retrieve the order items passed from OrderFragment
        val orderItemsArray = arguments?.getParcelableArray("orderItems")
        val restaurant = arguments?.getParcelable<Restaurant>("restaurant")
        orderItems = orderItemsArray?.filterIsInstance<MenuItemWithQuantity>()?.toMutableList() ?: mutableListOf()

        binding.checkoutRecyclerView.layoutManager = LinearLayoutManager(context)
        orderItemAdapter = OrderItemAdapter(orderItems) { orderItem, quantity ->
            orderItem.quantity = quantity
        }
        binding.checkoutRecyclerView.adapter = orderItemAdapter

        binding.placeOrderBtn.setOnClickListener {
            if (restaurant != null) {
                placeOrder(orderItems, binding.addressEditText.text.toString(), binding.specialInstructionsEditText.text.toString(), restaurant)
            }
        }

        binding.modifyOrderBtn.setOnClickListener{
            findNavController().navigateUp()
        }

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.nav_recent_orders -> {
                    findNavController().navigate(R.id.recentOrdersFragment)
                    true
                }
                R.id.nav_home -> {
                    findNavController().navigate(R.id.homeFragment)
                    true
                }R.id.nav_sign_out ->{
                val auth = FirebaseAuth.getInstance()
                auth.signOut()
                Toast.makeText(requireContext(), "Signed out", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.signUpFragment)
                true
            }
                else -> false
            }
        }

        return binding.root
    }

    private fun placeOrder(items: List<MenuItemWithQuantity>, deliveryAddress: String, specialInstructions: String, restaurant: Restaurant) {
        val totalPrice = calculateTotalPrice(items)
        val currentTime = System.currentTimeMillis().toString()

        val distance = 10.0
        val deliveryTime = calculateDeliveryTime(distance)

        // Create the list of order items
        val orderItems = items.map { item ->
            OrderItem(
                item.menuItem.name ?: "",
                item.menuItem.price ?: 0.0,
                item.quantity
            )
        }

        // Create the order object
        val order = Order(
            restaurant,
            orderItems,
            totalPrice,
            deliveryAddress,
            specialInstructions,
            currentTime,
            deliveryTime,
            Timestamp.now()
        )

        // Save the order to Firestore
        val db = FirebaseFirestore.getInstance()
        val orderCollection = db.collection("orders")
        orderCollection.add(order)
            .addOnSuccessListener {
                Log.d("OrderFragment", "Order successfully saved.")
            }
            .addOnFailureListener { exception ->
                Log.e("OrderFragment", "Error saving order: ", exception)
            }

        scheduleDeliveryAlarm(deliveryTime)

        val action = CheckoutFragmentDirections.actionCheckoutFragmentToOrderDetailsFragment(order)
        findNavController().navigate(action)
    }

    private fun calculateDeliveryTime(distance: Double): Double {
        val randomMultiplier = (5..100).random()
        return (distance / 100) * randomMultiplier
    }

    private fun calculateTotalPrice(items: List<MenuItemWithQuantity>): Double {
        return items.sumOf {
            (it.menuItem.price ?: 0.0) * it.quantity
        }
    }

    private fun scheduleDeliveryAlarm(minutes: Double) {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), DeliveryAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val triggerTime = Calendar.getInstance().apply {
            add(Calendar.MINUTE, minutes.toInt())
        }.timeInMillis
        alarmManager.canScheduleExactAlarms()
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
    }





}
