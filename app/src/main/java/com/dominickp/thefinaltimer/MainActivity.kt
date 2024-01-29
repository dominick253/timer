package com.dominickp.thefinaltimer

import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dominickp.thefinaltimer.ui.theme.TheFinalTimerTheme
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TheFinalTimerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WorkoutTimer()
                }
            }
        }
    }
}

@Composable
fun WorkoutTimer() {
    var workTime by remember { mutableStateOf(0) }
    var restTime by remember { mutableStateOf(0) }
    var intervals by remember { mutableStateOf(0) }
    var currentInterval by remember { mutableStateOf(0) }
    var timerState by remember { mutableStateOf("Workout Timer") }
    var backgroundColor by remember { mutableStateOf(Color.White) }
    var timerText by remember { mutableStateOf("") }
    var timer: CountDownTimer? = null

    LaunchedEffect(key1 = currentInterval, key2 = timerState) {
        if (currentInterval <= intervals && (workTime > 0 || restTime > 0)) {
            when (timerState) {
                "Work" -> {
                    backgroundColor = Color.Green
                    timer = object : CountDownTimer(workTime * 1000L, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            timerText = "Work: ${millisUntilFinished / 1000}s"
                        }

                        override fun onFinish() {
                            // Play rest sound here
                            timerState = "Rest"
                        }
                    }.start()
                }
                "Rest" -> {
                    backgroundColor = Color.Blue
                    timer = object : CountDownTimer(restTime * 1000L, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            timerText = "Rest: ${millisUntilFinished / 1000}s"
                        }

                        override fun onFinish() {
                            if (currentInterval < intervals) {
                                // Play work sound here
                                currentInterval++
                                timerState = "Work"
                            } else {
                                // Play finished sound here
                                timerState = "DONE!!!"
                                backgroundColor = Color.White
                            }
                        }
                    }.start()
                }
                else -> Unit
            }
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = timerState, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        // Text fields and other UI elements here...

        Row {
            Button(onClick = {
                currentInterval = 1
                timerState = "Work"
                // Play work sound here
            }) {
                Text("Start")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = {
                timer?.cancel()
                currentInterval = 0
                timerState = "Workout Timer"
                backgroundColor = Color.White
            }) {
                Text("Stop")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WorkoutTimerPreview() {
    TheFinalTimerTheme {
        WorkoutTimer()
    }
}
