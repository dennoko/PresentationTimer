package com.example.presentationtimer.model

import android.content.Context
import android.content.Context.POWER_SERVICE
import android.os.CountDownTimer
import android.os.PowerManager
import android.os.Vibrator
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CountDownManager(
    powerManager: PowerManager,
    vibrator: Vibrator
) {
    // manage the countdown timer
    val wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "CountDownManager:WakeLock")
    val vibrator = vibrator

    suspend fun startCountDown(setMinutes: Int) {
        wakeLock.acquire(setMinutes*60000L)
        // 1分おきに残り時間を振動で知らせる。 5分は長めの振動、1分は短めの振動とし、回数によって残り時間を表す。
        withContext(Dispatchers.Default) {
            for (i in setMinutes downTo 1) {
                val five = i/5
                val one = i%5
                withContext(Dispatchers.IO) {
                    for (j in 1..five) {
                        vibrator.vibrate(500)
                        Thread.sleep(1000)
                    }
                    for (j in 1..one) {
                        vibrator.vibrate(100)
                        Thread.sleep(1000)
                    }
                }
                Log.d("CountDownTest", "Remaining time: $i minutes")

                Thread.sleep(5000L)
            }
        }

        wakeLock.release()
    }

}