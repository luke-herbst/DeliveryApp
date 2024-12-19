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
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import iu.c323.fall2024.afinal.databinding.FragmentCheckoutBinding
import iu.c323.fall2024.afinal.databinding.FragmentRecentOrdersBinding
import iu.c323.fall2024.afinal.model.Order
import java.util.Calendar


class RecentOrdersFragment : Fragment() {

    private lateinit var binding: FragmentRecentOrdersBinding
    private lateinit var orderRecyclerView: RecyclerView
    private lateinit var orderAdapter: RecentOrderAdapter
    private val orderList = mutableListOf<Order>()
    private lateinit var db: FirebaseFirestore

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        fetchRecentOrders()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecentOrdersBinding.inflate(inflater, container, false)
        db = FirebaseFirestore.getInstance()

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



    private fun fetchRecentOrders() {
        db.collection("orders")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val order = document.toObject(Order::class.java)
                    orderList.add(order)
                }

                orderAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("RecentOrderFragment", "Error fetching orders", exception)
            }
    }

    private fun setupRecyclerView() {
        orderRecyclerView = view?.findViewById(R.id.orderRecyclerView) ?: return
        orderAdapter = RecentOrderAdapter(orderList) { order ->
            orderAgain(order)
        }
        orderRecyclerView.layoutManager = LinearLayoutManager(context)
        orderRecyclerView.adapter = orderAdapter
    }

    private fun orderAgain(order: Order) {
        order.timestamp = Timestamp.now()
        order.orderTime = System.currentTimeMillis().toString()
        // Save the order to Firestore
        val db = FirebaseFirestore.getInstance()
        val orderCollection = db.collection("orders") // Firestore collection for orders
        orderCollection.add(order)
            .addOnSuccessListener {
                Log.d("OrderFragment", "Order successfully saved.")
                order.deliveryTime?.let { scheduleDeliveryAlarm(it) }
                val action = RecentOrdersFragmentDirections.actionRecentOrdersFragmentToOrderDetailsFragment(order)
                findNavController().navigate(action)
            }
            .addOnFailureListener { exception ->
                Log.e("OrderFragment", "Error saving order: ", exception)
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
