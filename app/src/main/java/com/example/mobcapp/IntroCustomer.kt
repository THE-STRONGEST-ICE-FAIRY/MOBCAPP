package com.example.mobcapp

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class IntroCustomer : AppCompatActivity() {

    private lateinit var customerImageView: ImageView
    private lateinit var okButton: Button
    private lateinit var nopeButton: Button
    private lateinit var timerTextView: TextView

    private val dialogue = arrayOf(
        "Hiiiiiiiiiiiiii!",
        "My name issssssssss",
        "EMU OTORI!!!!!",
        "Emu means smile!",
        "...",
        "WONDERHOY!!! 🎪✨",
        "",
        "If you're here, then you should have already ordered something from the Laboratory Activity 3.",
        "Erm I mean... Ordering Menu",
        "Moving on, this is the customer menu.",
        "This is where you can meet with customers that would request orders.",
        "Who's the customer?",
        "That would be me, EMU OTORI!",
        "I'm here to teach newbies like you on how to give out orders to customers!",
        "Are you a newbie though?" // 🛑 **THIS IS THE BRANCHING POINT**
    )

    private val dialogueNo = arrayOf(
        "I see!",
        "If that's the case then you can skip this tutorial.",
        "So long!",
        "WONDERHOY!!! 🎪✨"
    )

    private val dialogueYes = arrayOf(
        "Then let's begin shall we?",
        "Here’s how you serve customers...",
        "<more dialogue>",
        "Good luck!"
    )

    private var dialogueIndex = 0
    private var currentDialogue = dialogue // **Holds the active dialogue array**
    private var isBranching = false // **Tracks if we're in YES/NO mode**

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_intro_customer)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.linearLayout_GameDifficulty)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        customerImageView = findViewById(R.id.customer_ImageView)
        okButton = findViewById(R.id.ok_Button)
        nopeButton = findViewById(R.id.nope_Button)
        timerTextView = findViewById(R.id.timer_TextView)

        timerTextView.visibility = View.INVISIBLE
        okButton.visibility = View.GONE
        nopeButton.visibility = View.GONE

        // **Load Image Safely**
        try {
            val inputStream = assets.open("customer/customer.png")
            val drawable = Drawable.createFromStream(inputStream, null)
            customerImageView.setImageDrawable(drawable)
        } catch (e: Exception) {
            e.printStackTrace()
            timerTextView.text = "Image Not Found!"
        }

        // **Start Timer on Activity Load**
        handler.postDelayed({
            okButton.visibility = View.VISIBLE
            timerTextView.visibility = View.VISIBLE
            timerTextView.text = currentDialogue[dialogueIndex]
        }, 5000)

        // **Set Click Listeners**
        okButton.setOnClickListener { nextDialogueWithDelay() }
        nopeButton.setOnClickListener { startNoBranch() } // **Handles NO choice**
    }

    private fun nextDialogueWithDelay() {
        okButton.isEnabled = false // **DISABLE BUTTON to prevent spam clicks**

        if (dialogueIndex < currentDialogue.size - 1) {
            dialogueIndex++
            timerTextView.text = currentDialogue[dialogueIndex]

            // **When reaching the "Are you a newbie?" question, switch to branching mode**
            if (currentDialogue.contentEquals(dialogue) && dialogueIndex == dialogue.size - 1) {
                okButton.isEnabled = true
                okButton.visibility = View.GONE
                nopeButton.visibility = View.VISIBLE // Show "No" button
                okButton.text = "Yes" // Change OK button to "Yes"
                okButton.visibility = View.VISIBLE
                isBranching = true // **Set branching flag**
                return
            }

            handler.postDelayed({ okButton.isEnabled = true }, 1000) // **Enable after 2 seconds**
        } else {
            // **If in branching mode and "Yes" is clicked, switch to YES dialogue**
            if (isBranching) {
                startYesBranch()
            } else {
                // **END NORMAL DIALOGUE**
//                timerTextView.text = "End of tutorial!"
                okButton.visibility = View.GONE
                nopeButton.visibility = View.GONE

                handler.postDelayed({ okButton.isEnabled = true }, 2000) // **Enable after 2 seconds**

                val intent = Intent(this, ActivityMainMenu::class.java)
                startActivity(intent)
            }
        }
    }

    private fun startNoBranch() {
        // **User chose "No", switch to NO dialogue**
        dialogueIndex = 0
        currentDialogue = dialogueNo
        okButton.visibility = View.VISIBLE
        nopeButton.visibility = View.GONE
        okButton.text = "Continue" // Change button text for NO path
        timerTextView.text = currentDialogue[dialogueIndex]
        isBranching = false // Reset branching flag
        nextDialogueWithDelay()
    }

    private fun startYesBranch() {
        // **User chose "Yes", switch to YES dialogue**
        dialogueIndex = 0
        currentDialogue = dialogueYes
        okButton.text = "Continue" // Change button text for YES path
        timerTextView.text = currentDialogue[dialogueIndex]
        nopeButton.visibility = View.GONE
        isBranching = false // Reset branching flag
        nextDialogueWithDelay()
    }
}
