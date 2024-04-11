package com.example.presentationtimer

import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.os.Vibrator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.presentationtimer.model.CountDownManager
import com.example.presentationtimer.ui.theme.PresentationTimerTheme
import com.example.presentationtimer.view.MainScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val powerManager = getSystemService(POWER_SERVICE) as PowerManager
        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        val countDownManager = CountDownManager(powerManager)

        setContent {
            val coroutineScope = rememberCoroutineScope()

            PresentationTimerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(
                        clickedButton = {
                            coroutineScope.launch {
                                withContext(Dispatchers.IO) {
                                    countDownManager.startCountDown(it.toInt(), vibrator)
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}
