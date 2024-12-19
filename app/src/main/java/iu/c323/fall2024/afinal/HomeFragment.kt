package iu.c323.fall2024.afinal

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import iu.c323.fall2024.afinal.databinding.FragmentHomeBinding
import iu.c323.fall2024.afinal.model.Order
import iu.c323.fall2024.afinal.model.Restaurant

private const val TAG = "HomeFragment"
class HomeFragment : Fragment() {

    private lateinit var recentRestaurantsRecyclerView: RecyclerView
    private lateinit var allRestaurantsRecyclerView: RecyclerView
    private lateinit var recentRestaurantsAdapter: RestaurantAdapter
    private lateinit var allRestaurantsAdapter: RestaurantAdapter
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.toolbar.inflateMenu(R.menu.navigation_menu)

        // Initialize Firestore
        db = FirebaseFirestore.getInstance()
        Log.d(TAG, "DB initiated")

        // Initialize RecyclerViews
        recentRestaurantsRecyclerView = binding.recentRestaurantsRecyclerView
        allRestaurantsRecyclerView = binding.allRestaurantsRecyclerView

        // Set up RecyclerViews
        recentRestaurantsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        allRestaurantsRecyclerView.layoutManager = LinearLayoutManager(context)

        // Initialize adapters for both lists
        recentRestaurantsAdapter = RestaurantAdapter{ restaurant ->
            // Navigate to the OrderFragment when a restaurant is clicked
            val action = HomeFragmentDirections.toRestaurantFragment(restaurant.id)
            view?.findNavController()?.navigate(action)
        }
        allRestaurantsAdapter = RestaurantAdapter{ restaurant ->
            // Navigate to the OrderFragment when a restaurant is clicked
            val action = HomeFragmentDirections.toRestaurantFragment(restaurant.id)
            view?.findNavController()?.navigate(action)
        }

        // Set adapters to RecyclerViews
        recentRestaurantsRecyclerView.adapter = recentRestaurantsAdapter
        allRestaurantsRecyclerView.adapter = allRestaurantsAdapter

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

        // Fetch data from Firestore
        fetchRecentRestaurants()
        fetchAllRestaurants()

        return binding.root
    }



    private fun fetchRecentRestaurants() {
        db.collection("orders")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(5)
            .get()
            .addOnSuccessListener { result ->
                val recentRestaurants = mutableListOf<Restaurant>()
                val orderCount = result.size()

                // If there are no orders, update the adapter with an empty list
                if (orderCount == 0) {
                    recentRestaurantsAdapter.submitList(recentRestaurants)
                    return@addOnSuccessListener
                }

                // Iterate through the orders to fetch the restaurant name and details
                for (document in result) {
                    val order = document.toObject(Order::class.java)
                    val restaurantName = order.restaurant?.name

                    db.collection("restaurants")
                        .whereEqualTo("name", restaurantName)
                        .limit(1)
                        .get()
                        .addOnSuccessListener { restaurantResult ->
                            val restaurant = restaurantResult.documents.firstOrNull()?.toObject(Restaurant::class.java)
                            restaurant?.let {
                                recentRestaurants.add(it)
                            }
                            if (recentRestaurants.size == orderCount) {
                                recentRestaurantsAdapter.submitList(recentRestaurants)
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.e("fetchRecentRestaurants", "Error fetching restaurant details", exception)
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("fetchRecentRestaurants", "Error fetching orders", exception)
            }
    }







    private fun fetchAllRestaurants() {
        db.collection("restaurants")
            .get()
            .addOnSuccessListener { result ->
                val allRestaurants = mutableListOf<Restaurant>()
                for (document in result) {
                    val restaurant = document.toObject(Restaurant::class.java)
                    allRestaurants.add(restaurant)
                }
                allRestaurantsAdapter.submitList(allRestaurants)
            }
    }
}
