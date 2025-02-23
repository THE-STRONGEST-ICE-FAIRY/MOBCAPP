package com.example.mobcapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        start()
    }

    private lateinit var cashier_Button: Button
    private lateinit var inventory_Button: Button
    fun start() {
        cashier_Button = findViewById(R.id.cashier_Button)
        inventory_Button = findViewById(R.id.inventory_Button)

        cashier_Button.setOnClickListener {
            val intent = Intent(this, Cashier::class.java)
            startActivity(intent)
        }

        inventory_Button.setOnClickListener {
            val intent = Intent(this, Inventory::class.java)
            startActivity(intent)
        }
    }
}