package com.example.mobcapp

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
    private lateinit var timerTextView: TextView

    private val dialogue = arrayOf(
        "Hiiiiiiiiiiiiii!",
        "My name issssssssss",
        "EMU OTORI!!!!!",
        "Emu means smile!",
        "...",
        "WONDERHOY!!! 🎪✨"
    )
    private var dialogueIndex = 0
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_intro_customer)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        customerImageView = findViewById(R.id.customer_ImageView)
        okButton = findViewById(R.id.ok_Button)
        timerTextView = findViewById(R.id.timer_TextView)

        timerTextView.visibility = View.INVISIBLE
        okButton.visibility = View.INVISIBLE

        try {
            val inputStream = assets.open("customer/customer.png")
            val drawable = Drawable.createFromStream(inputStream, null)
            customerImageView.setImageDrawable(drawable)
        } catch (e: Exception) {
            e.printStackTrace()
            timerTextView.text = "Image Not Found!"
        }

        handler.postDelayed({
            okButton.visibility = View.VISIBLE
            timerTextView.visibility = View.VISIBLE
            timerTextView.text = dialogue[dialogueIndex]
        }, 5000)

        okButton.setOnClickListener {
            nextDialogueWithDelay()
        }
    }

    private fun nextDialogueWithDelay() {
        if (dialogueIndex < dialogue.size - 1) {
            okButton.isEnabled = false
            dialogueIndex++
            timerTextView.text = dialogue[dialogueIndex]

            handler.postDelayed({
                okButton.isEnabled = true
            }, 2000)
        } else {
//            timerTextView.text = "End"
            okButton.visibility = View.GONE
        }
    }
}
