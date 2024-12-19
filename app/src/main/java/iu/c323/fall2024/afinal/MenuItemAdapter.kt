package iu.c323.fall2024.afinal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import iu.c323.fall2024.afinal.model.MenuItem
import iu.c323.fall2024.afinal.model.MenuItemWithQuantity

class MenuItemAdapter(
    private var menuItemsWithQuantity: List<MenuItemWithQuantity>, // List of MenuItemWithQuantity
    private val onQuantityChange: (MenuItemWithQuantity, Int) -> Unit
) : RecyclerView.Adapter<MenuItemAdapter.MenuItemViewHolder>() {

    inner class MenuItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.menuItemName)
        val costTextView: TextView = itemView.findViewById(R.id.menuItemPrice)
        val quantityTextView: TextView = itemView.findViewById(R.id.menuItemQuantity)
        val btnPlus: ImageButton = itemView.findViewById(R.id.btnPlus)
        val btnMinus: ImageButton = itemView.findViewById(R.id.btnMinus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
        return MenuItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuItemViewHolder, position: Int) {
        val menuItemWithQuantity = menuItemsWithQuantity[position]
        holder.nameTextView.text = menuItemWithQuantity.menuItem.name
        holder.costTextView.text = String.format("$%.2f", menuItemWithQuantity.menuItem.price)

        var quantity = menuItemWithQuantity.quantity

        holder.quantityTextView.text = quantity.toString()

        holder.btnPlus.setOnClickListener {
            quantity += 1
            menuItemWithQuantity.quantity = quantity
            onQuantityChange(menuItemWithQuantity, quantity)
            holder.quantityTextView.text = quantity.toString()
        }

        holder.btnMinus.setOnClickListener {
            if (quantity > 0) {
                quantity -= 1
                menuItemWithQuantity.quantity = quantity
                onQuantityChange(menuItemWithQuantity, quantity)
                holder.quantityTextView.text = quantity.toString()
            }
        }
    }

    override fun getItemCount(): Int = menuItemsWithQuantity.size

    fun updateMenuItems(updatedItems: List<MenuItemWithQuantity>) {
        this.menuItemsWithQuantity = updatedItems
        notifyDataSetChanged()
    }
}

