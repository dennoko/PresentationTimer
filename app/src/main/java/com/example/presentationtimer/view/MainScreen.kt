package com.example.presentationtimer.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.min

@Composable
fun MainScreen(
    clickedButton: (String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var minutes by remember { mutableStateOf("") }

        OutlinedTextField(
            value = minutes,
            onValueChange = {
                if(it.toIntOrNull() == null) {
                    return@OutlinedTextField
                }
                minutes = it
            },
            // numeric keyboard
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        OutlinedButton(
            onClick = {
                clickedButton(minutes)
                coroutineScope.launch {
                    withContext(Dispatchers.Default) {
                        delay(60000L * min(minutes.toInt(), 10))
                        minutes = ""
                    }
                }
            }
        ) {
            Text(text = "Start")
        }
    }
}