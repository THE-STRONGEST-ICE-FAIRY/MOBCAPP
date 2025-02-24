package com.example.mobcapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductivityAdapter(
    private val context: Context,
    private val itemList: List<DataClassProductivityItem>,
    private val onItemClick: (DataClassProductivityItem) -> Unit
) : RecyclerView.Adapter<ProductivityAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.item_name)
        val priceTextView: TextView = view.findViewById(R.id.item_price)
        val descriptionTextView: TextView = view.findViewById(R.id.item_description)
        val orderButton: Button = view.findViewById(R.id.order_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_productivity, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.nameTextView.text = item.name
        holder.priceTextView.text = "₱${item.price}"
        holder.descriptionTextView.text = item.description
        holder.orderButton.text = if (item.ordered) "Ordered" else "Order"

        holder.orderButton.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = itemList.size
}
