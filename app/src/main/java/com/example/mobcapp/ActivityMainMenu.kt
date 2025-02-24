package com.example.mobcapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ActivityMainMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // leave blank
            }
        })

        start()
    }

    private lateinit var cashier_Button: Button
    private lateinit var game_Button: Button
    private lateinit var about_Button: Button
    fun start() {
        cashier_Button = findViewById(R.id.cashier_Button)
        game_Button = findViewById(R.id.game_Button)
        about_Button = findViewById(R.id.about_Button)

        cashier_Button.setOnClickListener {
            startActivity(Intent(this, ActivityProductivity::class.java))
        }
        game_Button.setOnClickListener {
            startActivity(Intent(this, ActivityGame::class.java))
        }
        about_Button.setOnClickListener {
            startActivity(Intent(this, ActivityAbout::class.java))
        }
    }
}