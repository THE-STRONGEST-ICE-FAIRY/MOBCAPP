package com.example.mobcapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ActivityGameDifficulty : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game_difficulty)
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

    private lateinit var button_Easy: Button
    private lateinit var button_Medium: Button
    private lateinit var button_Hard: Button
    private lateinit var button_BackToMainMenu: Button
    private lateinit var leaderboardsLayout: LinearLayout
    private lateinit var recyclerViewLeaderboards: RecyclerView
    private lateinit var leaderboardAdapter: LeaderboardAdapter
    private lateinit var databaseHelper: LeaderboardDatabaseHelper

    fun init() {
        button_Easy = findViewById(R.id.button_Easy)
        button_Medium = findViewById(R.id.button_Medium)
        button_Hard = findViewById(R.id.button_Hard)
        button_BackToMainMenu = findViewById(R.id.button_BackToMainMenu)

        button_Easy.setOnClickListener {
            startActivity(Intent(this, ActivityGame::class.java).putExtra("DIFFICULTY", 1))
        }
        button_Medium.setOnClickListener {
            startActivity(Intent(this, ActivityGame::class.java).putExtra("DIFFICULTY", 2))
        }
        button_Hard.setOnClickListener {
            startActivity(Intent(this, ActivityGame::class.java).putExtra("DIFFICULTY", 3))
        }
        button_BackToMainMenu.setOnClickListener {
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
            finish()
        }

        leaderboardsLayout = findViewById(R.id.layout_Leaderboards)
        recyclerViewLeaderboards = findViewById(R.id.recyclerView_Leaderboards)
        databaseHelper = LeaderboardDatabaseHelper(this)
        leaderboardAdapter = LeaderboardAdapter(emptyList())

        recyclerViewLeaderboards.layoutManager = LinearLayoutManager(this)
        recyclerViewLeaderboards.adapter = leaderboardAdapter

        leaderboardAdapter.updateData(databaseHelper.getTopScores())
    }
}