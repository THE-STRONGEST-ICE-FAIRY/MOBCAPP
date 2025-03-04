package com.example.mobcapp

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import java.util.Locale

class SampleAdapter(
    private val context: Context,
    private val itemList: List<ItemsDataClass>,
    private val itemDatabase: ItemsDatabase,
    private val cashier: ActivityGame,
    private val onItemClick: (ItemsDataClass) -> Unit  // Click listener
) : RecyclerView.Adapter<SampleAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.sample_TextView)
        val desc: TextView = view.findViewById(R.id.sample_TextView2)
        val price: TextView = view.findViewById(R.id.sample_TextView3)
        val image: ImageView = view.findViewById(R.id.imageView)
        val imageButton: ImageButton = view.findViewById(R.id.button)

        init {
            view.setOnClickListener {
                onItemClick(itemList[adapterPosition]) // Handle click
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_sample_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val id = itemList[position].id
        val ordered = itemList[position].ordered
        val longDesc = itemList[position].descLong
        holder.textView.text = itemList[position].name
        holder.desc.text = itemList[position].desc
        holder.price.text = String.format(Locale.US, "$%.2f", itemList[position].price)
        holder.image.setImageDrawable(Drawable.createFromStream(context.assets.open(itemList[position].image), null))
        holder.imageButton.setImageDrawable(Drawable.createFromStream(context.assets.open(if (ordered == 0) "utility/moreinfo.png" else "utility/delete.png"), null))
        holder.imageButton.isEnabled = ordered != 0

        holder.imageButton.setOnClickListener {
            if (ordered == 1) {
                itemDatabase.deleteData(itemList[position].id)
//                Snackbar.make(it, "${holder.textView.text} removed from cart", Snackbar.LENGTH_SHORT).show()
            }
            else {
                cashier.layout_Menu.visibility = View.GONE
                cashier.imageView_Item.setImageDrawable(holder.image.drawable)
                cashier.textView_ItemName.text = holder.textView.text
                cashier.textView_ItemPrice.text = holder.price.text
                cashier.textView_ItemDesc.text = holder.desc.text
                cashier.textView_ItemLongDesc.text = longDesc
            }
            cashier.update(0)
            cashier.updateTextViews()

        }
    }

    override fun getItemCount() = itemList.size
}
