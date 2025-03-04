package com.example.mobcapp

import android.graphics.PorterDuff
import android.view.MotionEvent
import android.widget.ImageButton

object ClickEffect {
    fun ImageButton.applyClickEffect(normalColor: Int, pressedColor: Int) {
        this.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> this.setColorFilter(pressedColor, PorterDuff.Mode.SRC_IN) // Pressed state
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> this.clearColorFilter() // Reset to normal
            }
            false
        }
    }
}