package iu.c323.fall2024.afinal

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import iu.c323.fall2024.afinal.model.Order
import java.util.Calendar

class RecentOrderAdapter(
    private val orderList: List<Order>,
    private val onOrderAgainClick: (Order) -> Unit
) : RecyclerView.Adapter<RecentOrderAdapter.OrderViewHolder>() {

    inner class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val restaurantName: TextView = view.findViewById(R.id.recent_order_restaurant_name)
        val totalPrice: TextView = view.findViewById(R.id.recent_order_total_price)
        val deliveryAddress: TextView = view.findViewById(R.id.recent_order_delivery_address)
        val specialInstructions: TextView = view.findViewById(R.id.recent_order_special_instructions)
        val orderDate: TextView = view.findViewById(R.id.recent_order_date)
        val orderTime: TextView = view.findViewById(R.id.recent_order_time)
        val orderAgainButton: Button = view.findViewById(R.id.order_again_btn)
        val orderItemsRecyclerView: RecyclerView = view.findViewById(R.id.recent_order_items_recyclerview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recent_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orderList[position]

        // Bind Order data to views
        holder.restaurantName.text = "Restaurant Name: " + (order.restaurant?.name ?: "")
        holder.totalPrice.text = "Total Price: $${order.totalPrice ?: 0.0}"
        holder.deliveryAddress.text = "Delivery Address: " + order.deliveryAddress
        holder.specialInstructions.text = "Special instructions: " + order.specialInstructions
        holder.orderDate.text = "Date: " + order.timestamp?.toDate()?.let { formatDate(it) }
        holder.orderTime.text = "Time: " + order.orderTime

        // Set up the RecyclerView for order items
        val orderItemsAdapter = OrderDetailsAdapter(order.items ?: emptyList())
        holder.orderItemsRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.orderItemsRecyclerView.adapter = orderItemsAdapter

        // Handle the "Order Again" button click
        holder.orderAgainButton.setOnClickListener {
            onOrderAgainClick(order)
        }

    }

    override fun getItemCount() = orderList.size

    private fun formatDate(date: Date): String {
        val format = SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault())
        return format.format(date)
    }



}
