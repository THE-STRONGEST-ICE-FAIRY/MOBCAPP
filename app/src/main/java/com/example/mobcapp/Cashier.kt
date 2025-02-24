package com.example.mobcapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class Cashier : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cashier)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        start()
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var orderCount_TextView: TextView
    private lateinit var orderTotalPrice_TextView: TextView
    private lateinit var viewOrders_Button: Button
    private lateinit var giveOrders_Button: Button
    private val itemDatabase by lazy { ItemsDatabase(this) }
    private val customerDatabase by lazy { CustomerDatabase(this) }
    private val customerPrefsDatabase by lazy { CustomerPrefsDatabase(this) }
    private var ordered = 0
    private fun start() {
        recyclerView = findViewById(R.id.cashier_RecyclerView)
        orderCount_TextView = findViewById(R.id.orderCount_TextView)
        orderTotalPrice_TextView = findViewById(R.id.orderTotalPrice_TextView)
        viewOrders_Button = findViewById(R.id.viewOrders_Button)
        giveOrders_Button = findViewById(R.id.giveOrders_Button)

        recyclerView.layoutManager = LinearLayoutManager(this)
//        update()

        itemDatabase.dropTable()
        itemDatabase.sampleInsert()
        customerDatabase.dropTable()
        customerDatabase.sampleInsert()
        customerPrefsDatabase.dropTable()
        customerPrefsDatabase.sampleInsert()

        viewOrders_Button.setOnClickListener {
            ordered = if (ordered == 0) 1 else 0
            viewOrders_Button.text = if (ordered == 0) "View Orders" else "View Menu"
            recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, (ordered != 0))
//            update()
        }
    }

//    fun update() {
//        val adapter = SampleAdapter(this, itemDatabase.getOrderedData(ordered), itemDatabase, this) { item ->
//            if (item.ordered == 0) {
//                itemDatabase.insertData(
//                    item.name,
//                    item.image,
//                    item.desc,
//                    item.price,
//                    ordered = 1,
//                    item.descLong,
//                    item.calories,
//                    item.grams,
//                    item.ingredients
//                )
//                Snackbar.make(findViewById(android.R.id.content), "${item.name} added to cart", Snackbar.LENGTH_SHORT).show()
//            }
//            updateTextViews()
//
//        }
//
//        recyclerView.adapter = adapter
//    }

    fun updateTextViews() {
        val orders = itemDatabase.getOrderedData(1)
        val totalAmount = orders.size
        val totalPrice = orders.sumOf { it.price }

        orderCount_TextView.text = "Orders: $totalAmount"
        orderTotalPrice_TextView.text = "Total: $totalPrice"
    }
}