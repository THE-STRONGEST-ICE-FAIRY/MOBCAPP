package com.example.mobcapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ActivityAbout : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_about)
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

        init()
    }

    private lateinit var button_LabAct3: Button
    private lateinit var button_MainMenu: Button

    private fun init() {
        button_LabAct3 = findViewById(R.id.button_LabAct3)
        button_MainMenu = findViewById(R.id.button_MainMenu)

        button_LabAct3.setOnClickListener {
            getSharedPreferences("AppSettings", MODE_PRIVATE).edit().putBoolean("hasCompletedOnboarding", false).apply()
            startActivity(Intent(this, MainActivity::class.java))
        }
        button_MainMenu.setOnClickListener {
            startActivity(Intent(this, ActivityMainMenu::class.java))
        }
    }
}