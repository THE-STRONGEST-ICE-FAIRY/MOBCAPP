package com.example.mobcapp
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OrderAdapter(private val orders: List<Order>, private val context: Context) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    private lateinit var mealData: Map<String, Pair<Double, Drawable?>>

    init {
        loadMealData()
    }

    private fun loadMealData() {
        mealData = mapOf(
            "Burger" to Pair(39.99, Drawable.createFromStream(context.assets.open("items/burger.png"), null)),
            "Spaghetti" to Pair(49.99, Drawable.createFromStream(context.assets.open("items/spaghetti.png"), null)),
            "Pizza" to Pair(99.99, Drawable.createFromStream(context.assets.open("items/pizza.png"), null)),
            "Chicken" to Pair(89.99, Drawable.createFromStream(context.assets.open("items/chicken.png"), null)),
            "Sandwich" to Pair(29.99, Drawable.createFromStream(context.assets.open("items/sandwich.png"), null)),
            "Tapsilog" to Pair(119.99, Drawable.createFromStream(context.assets.open("items/tapsilog.png"), null)),
            "Tilapia" to Pair(109.99, Drawable.createFromStream(context.assets.open("items/tilapia.png"), null)),
            "Halo-Halo" to Pair(99.99, Drawable.createFromStream(context.assets.open("items/halohalo.png"), null)),
            "Lumpiang Shanghai" to Pair(89.99, Drawable.createFromStream(context.assets.open("items/lumpia.png"), null)),
            "Puto" to Pair(49.99, Drawable.createFromStream(context.assets.open("items/puto.png"), null))
        )
    }

    class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mealName: TextView = view.findViewById(R.id.mealName_TextView)
        val orderAmount: TextView = view.findViewById(R.id.orderAmount_TextView)
        val totalPrice: TextView = view.findViewById(R.id.totalPrice_TextView)
        val fries_TextView: TextView = view.findViewById(R.id.fries_TextView)
        val drink_TextView: TextView = view.findViewById(R.id.drink_TextView)
        val orderImage_ImageView: ImageView = view.findViewById(R.id.orderImage_ImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.mealName.text = order.mealName
        holder.orderAmount.text = "Amount: ${order.orderAmount}"
        holder.totalPrice.text = "Total: ₱${"%.2f".format(order.totalPrice)}"
        holder.fries_TextView.text = "Fries: ${order.friesSize}"
        holder.drink_TextView.text = "Drink: ${order.drinkSize}"
        val (price, imageResId) = mealData[order.mealName.toString()] ?: return
        holder.orderImage_ImageView.setImageDrawable(imageResId)
    }

    override fun getItemCount() = orders.size
}