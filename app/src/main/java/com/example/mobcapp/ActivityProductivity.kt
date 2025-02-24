package com.example.mobcapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class ActivityProductivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_productivity)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // No back press action
            }
        })

        init()
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var orderCount_TextView: TextView
    private lateinit var orderTotalPrice_TextView: TextView
    private lateinit var viewOrders_Button: Button
    private lateinit var giveOrders_Button: Button
    private lateinit var button_MainMenu_Productivity: Button
    private val itemDatabase by lazy { DatabaseProductivityItems(this) }
    private var ordered = 0

    private fun init() {
        recyclerView = findViewById(R.id.cashier_RecyclerView)
        orderCount_TextView = findViewById(R.id.orderCount_TextView)
        orderTotalPrice_TextView = findViewById(R.id.orderTotalPrice_TextView)
        viewOrders_Button = findViewById(R.id.viewOrders_Button)
        giveOrders_Button = findViewById(R.id.giveOrders_Button)
        button_MainMenu_Productivity = findViewById(R.id.button_MainMenu_Productivity)

        recyclerView.layoutManager = LinearLayoutManager(this)
        updateRecyclerView()

        viewOrders_Button.setOnClickListener {
            ordered = if (ordered == 0) 1 else 0
            viewOrders_Button.text = if (ordered == 0) "View Orders" else "View Menu"
            updateRecyclerView()
        }

        button_MainMenu_Productivity.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Confirmation")
                .setMessage("Are you sure you want to go back to the main menu? Your sales' data are saved.")
                .setPositiveButton("Yes") { _, _ ->
                    startActivity(Intent(this, MainActivity::class.java))
                }
                .setNegativeButton("No", null)
                .show()
        }
    }

    private fun updateRecyclerView() {
        val items = itemDatabase.getAllItems().filter { it.ordered == (ordered == 1) }
        val adapter = ProductivityAdapter(this, items) { item ->
            if (!item.ordered) {
                itemDatabase.insertItem(
                    ordered = true,
                    name = item.name,
                    price = item.price,
                    description = item.description
                )
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "${item.name} added to cart",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            updateTextViews()
            updateRecyclerView()
        }
        recyclerView.adapter = adapter
    }

    fun updateTextViews() {
        val orders = itemDatabase.getAllItems().filter { it.ordered }
        val totalAmount = orders.size
        val totalPrice = orders.sumOf { it.price }

        orderCount_TextView.text = "Orders: $totalAmount"
        orderTotalPrice_TextView.text = "Total: $totalPrice"
    }
}
