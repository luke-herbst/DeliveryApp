package iu.c323.fall2024.afinal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import iu.c323.fall2024.afinal.model.OrderItem

class OrderDetailsAdapter(private val items: List<OrderItem>) : RecyclerView.Adapter<OrderDetailsAdapter.OrderDetailsViewHolder>() {

    class OrderDetailsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemName: TextView = itemView.findViewById(R.id.itemName)
        val itemQuantity: TextView = itemView.findViewById(R.id.itemQuantity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order_detail, parent, false)
        return OrderDetailsViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderDetailsViewHolder, position: Int) {
        val item = items[position]
        holder.itemName.text = item.itemName
        holder.itemQuantity.text = "Quantity: " + item.quantity
    }

    override fun getItemCount(): Int = items.size
}
