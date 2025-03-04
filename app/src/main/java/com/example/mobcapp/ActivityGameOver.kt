package com.example.mobcapp

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mobcapp.ClickEffect.applyClickEffect

class ActivityGameOver : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game_over)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.linearLayout_GameOver)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        Toast.makeText(this, "message 2", Toast.LENGTH_SHORT).show()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // leave blank
            }
        })

        init()
    }

    private lateinit var linearLayout_GameOver: LinearLayout
    private lateinit var scoreDisplay: TextView
    private lateinit var inputNameLayout: LinearLayout
    private lateinit var editTextName: EditText
    private lateinit var buttonConfirmName: ImageButton
    private lateinit var leaderboardsLayout: LinearLayout
    private lateinit var recyclerViewLeaderboards: RecyclerView
    private lateinit var leaderboardAdapter: LeaderboardAdapter
    private lateinit var databaseHelper: LeaderboardDatabaseHelper
    private lateinit var imageView_Leaderboard: ImageView
    private lateinit var imageView_LeaderboardName: ImageView
    private lateinit var imageView_LeaderboardScore: ImageView
    private lateinit var buttonBackToMainMenu: ImageButton

    private fun init() {
        linearLayout_GameOver = findViewById(R.id.linearLayout_GameOver)
        scoreDisplay = findViewById(R.id.textView_ScoreDisplay)
        inputNameLayout = findViewById(R.id.layout_InputName)
        editTextName = findViewById(R.id.editText_InputName)
        buttonConfirmName = findViewById(R.id.button_ConfirmName)
        leaderboardsLayout = findViewById(R.id.layout_Leaderboards)
        recyclerViewLeaderboards = findViewById(R.id.recyclerView_Leaderboards)
        databaseHelper = LeaderboardDatabaseHelper(this)
        imageView_Leaderboard = findViewById(R.id.imageView_Leaderboard)
        imageView_LeaderboardName = findViewById(R.id.imageView_LeaderboardName)
        imageView_LeaderboardScore = findViewById(R.id.imageView_LeaderboardScore)
        buttonBackToMainMenu = findViewById(R.id.buttonBackToMainMenu)

        linearLayout_GameOver.background = Drawable.createFromStream(assets.open("gui/MainMenuBackground.png"), null)
        imageView_Leaderboard.setImageDrawable(Drawable.createFromStream(assets.open("gui/GameDifficultyLeaderboard.png"), null))
        imageView_LeaderboardName.setImageDrawable(Drawable.createFromStream(assets.open("gui/GameDifficultyName.png"), null))
        imageView_LeaderboardScore.setImageDrawable(Drawable.createFromStream(assets.open("gui/GameDifficultyScore.png"), null))
        buttonConfirmName.setImageDrawable(Drawable.createFromStream(assets.open("gui/GameOverConfirmName.png"), null))
        buttonBackToMainMenu.setImageDrawable(Drawable.createFromStream(assets.open("gui/GameOverBackToMainMenu.png"), null))

        buttonConfirmName.applyClickEffect(Color.WHITE, Color.LTGRAY)
        buttonBackToMainMenu.applyClickEffect(Color.WHITE, Color.LTGRAY)

        leaderboardsLayout.visibility = View.GONE

        scoreDisplay.text = "Total Sales: " + intent.getIntExtra("SCORE", 0) + " GoldBurgs\nEnter your name:"

        leaderboardAdapter = LeaderboardAdapter(emptyList())
        recyclerViewLeaderboards.layoutManager = LinearLayoutManager(this)
        recyclerViewLeaderboards.adapter = leaderboardAdapter

        loadLeaderboardData()

        buttonConfirmName.setOnClickListener {
            val playerName = editTextName.text.toString().trim()
            val playerScore = intent.getIntExtra("SCORE", 0)

            scoreDisplay.text = "$playerName's Sales: " + intent.getIntExtra("SCORE", 0) + " GoldBurgs"

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