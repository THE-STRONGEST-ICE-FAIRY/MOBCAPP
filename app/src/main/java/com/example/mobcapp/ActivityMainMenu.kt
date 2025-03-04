package com.example.mobcapp

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mobcapp.ClickEffect.applyClickEffect

class ActivityMainMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_menu)
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

        val itemDatabase by lazy { ItemsDatabase(this) }
        val customerDatabase by lazy { CustomerDatabase(this) }
        val customerPrefsDatabase by lazy { CustomerPrefsDatabase(this) }
        val leaderboardDatabaseHelper by lazy { LeaderboardDatabaseHelper(this) }
//        itemDatabase.dropTable()
//        itemDatabase.sampleInsert()
//        customerDatabase.dropTable()
//        customerDatabase.sampleInsert()
//        customerPrefsDatabase.dropTable()
//        customerPrefsDatabase.sampleInsert()

//        leaderboardDatabaseHelper.clearLeaderboard()

        start()
    }

    private lateinit var imageView_Title: ImageView
    private lateinit var cashier_Button: Button
    private lateinit var game_Button: ImageButton
    private lateinit var about_Button: ImageButton
    private lateinit var mainLayout: LinearLayout
    fun start() {
        imageView_Title = findViewById(R.id.imageView_Title)
        cashier_Button = findViewById(R.id.cashier_Button)
        game_Button = findViewById(R.id.game_Button)
        about_Button = findViewById(R.id.about_Button)
        mainLayout = findViewById(R.id.linearLayout_GameDifficulty)

        imageView_Title.setImageDrawable(Drawable.createFromStream(assets.open("gui/MainMenuTitle.png"), null))
        game_Button.setImageDrawable(Drawable.createFromStream(assets.open("gui/MainMenuGame.png"), null))
        about_Button.setImageDrawable(Drawable.createFromStream(assets.open("gui/MainMenuCredits.png"), null))
        mainLayout.background = Drawable.createFromStream(assets.open("gui/MainMenuBackground.png"), null)

        cashier_Button.setOnClickListener {
            startActivity(Intent(this, ActivityProductivity::class.java))
        }
        game_Button.setOnClickListener {
            startActivity(Intent(this, ActivityGameDifficulty::class.java))
        }
        game_Button.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    game_Button.setImageDrawable(Drawable.createFromStream(assets.open("gui/MainMenuGamePressed.png"), null))
                    false
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    game_Button.setImageDrawable(Drawable.createFromStream(assets.open("gui/MainMenuGame.png"), null))
                    false
                }
                else -> false
            }
        }
        game_Button.applyClickEffect(Color.WHITE, Color.LTGRAY)
        about_Button.setOnClickListener {
            startActivity(Intent(this, ActivityAbout::class.java))
        }
        about_Button.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    about_Button.setImageDrawable(Drawable.createFromStream(assets.open("gui/MainMenuCreditsPressed.png"), null))
                    false
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    about_Button.setImageDrawable(Drawable.createFromStream(assets.open("gui/MainMenuCredits.png"), null))
                    false
                }
                else -> false
            }
        }
        about_Button.applyClickEffect(Color.WHITE, Color.LTGRAY)
    }
}