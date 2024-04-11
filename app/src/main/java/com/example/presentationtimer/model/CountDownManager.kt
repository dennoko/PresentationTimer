package com.example.presentationtimer.model

import android.os.Build
import android.os.PowerManager
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class CountDownManager(
    powerManager: PowerManager,
) {
    // manage the countdown timer
    val wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "CountDownManager:WakeLock")

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun startCountDown(setMinutes: Int, vibrator: Vibrator) {
        wakeLock.acquire(setMinutes*60000L)
        // 1分おきに残り時間を振動で知らせる。 5分は長めの振動、1分は短めの振動とし、回数によって残り時間を表す。
        withContext(Dispatchers.Default) {
            for (i in setMinutes downTo 1) {
                val five = i/5
                val one = i%5
                val vibratorShortEffect = VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE)
                val vibratorLongEffect = VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)
                var vibrateTime = 0L
                for (j in 1..five) {
                    vibrator.vibrate(vibratorLongEffect)
                    Log.d("CountDownTest", "Vibrate: 5")
                    delay(1000)
                    vibrateTime += 1500
                }
                for (j in 1..one) {
                    vibrator.vibrate(vibratorShortEffect)
                    Log.d("CountDownTest", "Vibrate: 1")
                    delay(1000)
                    vibrateTime += 1100
                }
                Log.d("CountDownTest", "Remaining time: $i minutes")

                Thread.sleep(60000L - vibrateTime)

                if (i == 1) {
                    val vibratorLastEffect = VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE)
                    vibrator.vibrate(vibratorLastEffect)
                    Thread.sleep(200)
                    vibrator.vibrate(vibratorLastEffect)
                }
            }
        }

        wakeLock.release()
    }

}