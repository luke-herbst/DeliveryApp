package iu.c323.fall2024.afinal

import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import iu.c323.fall2024.afinal.databinding.FragmentCheckoutBinding
import iu.c323.fall2024.afinal.databinding.FragmentOrderDetailsBinding
import iu.c323.fall2024.afinal.model.Order
import java.text.DateFormat
import java.util.Calendar
import java.util.Locale

class OrderDetailsFragment : Fragment() {

    private lateinit var order: Order
    private lateinit var binding: FragmentOrderDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderDetailsBinding.inflate(inflater, container, false)

        // Retrieve the order from the arguments
        order = arguments?.getParcelable("order")!!

        binding.orderDetailsRestaurantName.text = "Order from: " + (order.restaurant?.name)
        binding.orderDetailsTotalPrice.text = "Price: $" + order.totalPrice.toString()
        binding.orderDetailsDeliveryAddress.text = "Delivery address: " + order.deliveryAddress
        binding.orderDetailsSpecialInstructions.text = "Special instructions: " + order.specialInstructions
        binding.orderDetailsOrderDate.text = "Date: " + DateFormat.getDateInstance().format(Calendar.getInstance().time)

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

        // Set up the RecyclerView
        val recyclerView = binding.orderDetailsItemsRecyclerview
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = order.items?.let { OrderDetailsAdapter(it) }

        // Set up the Track Order button
        binding.trackOrderButton.setOnClickListener {
            val addressString = order.deliveryAddress ?: ""
            val latLongPair = getLatLongFromAddress(addressString)
            if (latLongPair != null) {
                val (destinationLat, destinationLng) = latLongPair
                val action = OrderDetailsFragmentDirections.actionOrderDetailsFragmentToMapFragment(order.restaurant, destinationLat.toString(), destinationLng.toString())
                findNavController().navigate(action)
            }
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

    private fun getLatLongFromAddress(address: String): Pair<Double, Double>? {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        return try {
            val addresses = geocoder.getFromLocationName(address, 1)
            if (!addresses.isNullOrEmpty()) {
                val location = addresses[0]
                Pair(location.latitude, location.longitude)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
