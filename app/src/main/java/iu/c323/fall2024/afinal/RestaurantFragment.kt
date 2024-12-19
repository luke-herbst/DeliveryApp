package iu.c323.fall2024.afinal

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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import iu.c323.fall2024.afinal.RestaurantFragmentDirections
import iu.c323.fall2024.afinal.databinding.FragmentRestaurantBinding
import iu.c323.fall2024.afinal.model.MenuItem
import iu.c323.fall2024.afinal.model.MenuItemWithQuantity
import iu.c323.fall2024.afinal.model.Restaurant

class RestaurantFragment : Fragment() {

    private lateinit var menuItemAdapter: MenuItemAdapter
    private lateinit var menuRecyclerView: RecyclerView
    private lateinit var db: FirebaseFirestore
    private lateinit var restaurantId: String
    private var menuItemsWithQuantity: MutableList<MenuItemWithQuantity> = mutableListOf()
    private lateinit var restaurant: Restaurant

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentRestaurantBinding.inflate(inflater, container, false)
        binding.toolbar.inflateMenu(R.menu.navigation_menu)

        db = FirebaseFirestore.getInstance()

        restaurantId = arguments?.getString("restaurantId") ?: ""

        menuRecyclerView = binding.menuRecyclerView
        menuRecyclerView.layoutManager = LinearLayoutManager(context)
        menuItemAdapter = MenuItemAdapter(menuItemsWithQuantity) { menuItemWithQuantity, quantity ->
            // Update the quantity of the menu item when changed
            menuItemWithQuantity.quantity = quantity
        }
        menuRecyclerView.adapter = menuItemAdapter

        // Fetch menu items
        fetchMenuItems()

        // Handle Checkout Button click
        binding.checkoutBtn.setOnClickListener {
            // Filter out items with quantity > 0
            val filteredMenuItems = menuItemsWithQuantity.filter { it.quantity > 0 }
            // Navigate to CheckoutFragment and pass the filtered menu items
            val action = RestaurantFragmentDirections.actionRestaurantFragmentToCheckoutFragment(filteredMenuItems.toTypedArray(), restaurant)
            findNavController().navigate(action)
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

    private fun fetchMenuItems() {
        db.collection("restaurants")
            .document(restaurantId)
            .collection("menu")
            .get()
            .addOnSuccessListener { result ->
                val items = mutableListOf<MenuItemWithQuantity>()
                for (document in result) {
                    val menuItem = document.toObject(MenuItem::class.java)
                    val menuItemWithQuantity = MenuItemWithQuantity(menuItem)
                    items.add(menuItemWithQuantity)
                }
                menuItemsWithQuantity = items
                menuItemAdapter.updateMenuItems(menuItemsWithQuantity)
            }
            .addOnFailureListener { exception ->
                Log.e("OrderFragment", "Error fetching menu items: ", exception)
            }

        db.collection("restaurants")
            .document(restaurantId) // Assuming restaurantId is passed or retrieved
            .get()
            .addOnSuccessListener { restaurantDocument ->
                restaurant = restaurantDocument.toObject(Restaurant::class.java)!!

                }
    }
}
