package com.example.mobcapp

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobcapp.ClickEffect.applyClickEffect

class ActivityGameDifficulty : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game_difficulty)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.linearLayout_GameDifficulty)) { v, insets ->
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

    private lateinit var databaseHelper: LeaderboardDatabaseHelper
    private lateinit var linearLayout_GameDifficulty: LinearLayout
    private lateinit var button_Easy: ImageButton
    private lateinit var button_Medium: ImageButton
    private lateinit var button_Hard: ImageButton
    private lateinit var leaderboardsLayout: LinearLayout
    private lateinit var imageView_Leaderboard: ImageView
    private lateinit var imageView_LeaderboardName: ImageView
    private lateinit var imageView_LeaderboardScore: ImageView
    private lateinit var recyclerViewLeaderboards: RecyclerView
    private lateinit var leaderboardAdapter: LeaderboardAdapter
    private lateinit var button_BackToMainMenu: ImageButton

    fun init() {
        linearLayout_GameDifficulty = findViewById(R.id.linearLayout_GameDifficulty)
        button_Easy = findViewById(R.id.button_Easy)
        button_Medium = findViewById(R.id.button_Medium)
        button_Hard = findViewById(R.id.button_Hard)
        imageView_Leaderboard = findViewById(R.id.imageView_Leaderboard)
        imageView_LeaderboardName = findViewById(R.id.imageView_LeaderboardName)
        imageView_LeaderboardScore = findViewById(R.id.imageView_LeaderboardScore)
        button_BackToMainMenu = findViewById(R.id.button_BackToMainMenu)

        linearLayout_GameDifficulty.background = Drawable.createFromStream(assets.open("gui/MainMenuBackground.png"), null)
        button_Easy.setImageDrawable(Drawable.createFromStream(assets.open("gui/GameDifficultyEasy.png"), null))
        button_Medium.setImageDrawable(Drawable.createFromStream(assets.open("gui/GameDifficultyMedium.png"), null))
        button_Hard.setImageDrawable(Drawable.createFromStream(assets.open("gui/GameDifficultyHard.png"), null))
        imageView_Leaderboard.setImageDrawable(Drawable.createFromStream(assets.open("gui/GameDifficultyLeaderboard.png"), null))
        imageView_LeaderboardName.setImageDrawable(Drawable.createFromStream(assets.open("gui/GameDifficultyName.png"), null))
        imageView_LeaderboardScore.setImageDrawable(Drawable.createFromStream(assets.open("gui/GameDifficultyScore.png"), null))
        button_BackToMainMenu.setImageDrawable(Drawable.createFromStream(assets.open("gui/GameDifficultyBack.png"), null))

        button_Easy.applyClickEffect(Color.WHITE, Color.LTGRAY)
        button_Medium.applyClickEffect(Color.WHITE, Color.LTGRAY)
        button_Hard.applyClickEffect(Color.WHITE, Color.LTGRAY)
        button_BackToMainMenu.applyClickEffect(Color.WHITE, Color.LTGRAY)

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