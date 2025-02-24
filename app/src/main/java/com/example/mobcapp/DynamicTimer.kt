package com.example.mobcapp

import android.os.CountDownTimer

class DynamicTimer(private val onTick: (Long) -> Unit, private val onFinish: () -> Unit) {
    private var countDownTimer: CountDownTimer? = null
    private var timeLeftInMillis: Long = 0

    fun startTimer(durationMillis: Long) {
        countDownTimer?.cancel() // Cancel any existing timer
        timeLeftInMillis = durationMillis

        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                onTick(millisUntilFinished / 1000) // Callback for UI update
            }

            override fun onFinish() {
                timeLeftInMillis = 0
                onFinish() // Callback when timer finishes
            }
        }.start()
    }

    fun stopTimer() {
        countDownTimer?.cancel()
    }

    fun restartTimer(newDurationMillis: Long) {
        startTimer(newDurationMillis)
    }

    // **🔥 NEW METHOD: Get Current Time Left in Seconds**
    fun getTimeLeft(): Long {
        return timeLeftInMillis / 1000
    }
}
