package com.example.mobcapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ActivityGameOver : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game_over)
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

    private lateinit var scoreDisplay: TextView
    private lateinit var inputNameLayout: LinearLayout
    private lateinit var editTextName: EditText
    private lateinit var buttonConfirmName: Button
    private lateinit var leaderboardsLayout: LinearLayout
    private lateinit var recyclerViewLeaderboards: RecyclerView
    private lateinit var buttonBackToMainMenu: Button
    private lateinit var leaderboardAdapter: LeaderboardAdapter
    private lateinit var databaseHelper: LeaderboardDatabaseHelper

    private fun init() {
        scoreDisplay = findViewById(R.id.textView_ScoreDisplay)
        inputNameLayout = findViewById(R.id.layout_InputName)
        editTextName = findViewById(R.id.editText_InputName)
        buttonConfirmName = findViewById(R.id.button_ConfirmName)
        leaderboardsLayout = findViewById(R.id.layout_Leaderboards)
        recyclerViewLeaderboards = findViewById(R.id.recyclerView_Leaderboards)
        buttonBackToMainMenu = findViewById(R.id.button_BackToMainMenu)

        leaderboardsLayout.visibility = View.GONE

        databaseHelper = LeaderboardDatabaseHelper(this)

        leaderboardAdapter = LeaderboardAdapter(emptyList())
        recyclerViewLeaderboards.layoutManager = LinearLayoutManager(this)
        recyclerViewLeaderboards.adapter = leaderboardAdapter

        loadLeaderboardData()

        buttonConfirmName.setOnClickListener {
            val playerName = editTextName.text.toString().trim()
            val playerScore = intent.getIntExtra("SCORE", 0)

            if (playerName.isNotEmpty()) {
                databaseHelper.insertEntry(playerName, playerScore)

                leaderboardsLayout.visibility = View.VISIBLE
                inputNameLayout.visibility = View.GONE

                loadLeaderboardData()
            }
        }

        buttonBackToMainMenu.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun loadLeaderboardData() {
        val leaderboardEntries = databaseHelper.getTopScores()
        leaderboardAdapter.updateData(leaderboardEntries)
    }
}