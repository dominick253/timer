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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TheFinalTimerTheme {
                WorkoutTimer()
            }
        }
    }
}

@Composable
fun WorkoutTimer() {
    var workTime by remember { mutableStateOf("") }
    var restTime by remember { mutableStateOf("") }
    var intervals by remember { mutableStateOf("") }
    var currentInterval by remember { mutableStateOf(1) }
    var timerState by remember { mutableStateOf("Workout Timer") }
    var backgroundColor by remember { mutableStateOf(Color.White) }
    var timerText by remember { mutableStateOf("") }
    var timer: CountDownTimer? = null

    LaunchedEffect(key1 = currentInterval, key2 = timerState) {
        timer?.cancel()
        when (timerState) {
            "Work" -> {
                backgroundColor = Color.Green
                val workSeconds = workTime.toIntOrNull() ?: 0
                timer = createTimer(
                    duration = workSeconds * 1000L,
                    onTick = { timerText = it },
                    onFinish = {
                        timerState = "Rest"
                        timerText = ""
                    }
                ).start()
            }
            "Rest" -> {
                backgroundColor = Color.Blue
                val restSeconds = restTime.toIntOrNull() ?: 0
                timer = createTimer(
                    duration = restSeconds * 1000L,
                    onTick = { timerText = it },
                    onFinish = {
                        if (currentInterval < (intervals.toIntOrNull() ?: 0)) {
                            currentInterval++
                            timerState = "Work"
                            timerText = ""
                        } else {
                            timerState = "DONE!!!"
                            backgroundColor = Color.White
                        }
                    }
                ).start()
            }
            "DONE!!!" -> {
                backgroundColor = Color.White
            }
        }
    }


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundColor
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = if (timerState == "Workout Timer") "Workout Timer" else "$timerState: $timerText")
            Spacer(modifier = Modifier.height(16.dp))

            if (timerState == "Workout Timer") {
                TextField(
                    value = workTime,
                    onValueChange = { workTime = it },
                    label = { Text("Work Time (sec)") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = restTime,
                    onValueChange = { restTime = it },
                    label = { Text("Rest Time (sec)") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = intervals,
                    onValueChange = { intervals = it },
                    label = { Text("Intervals") }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            Text(text = "Interval: $currentInterval")
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                if (timerState == "Workout Timer") {
                    currentInterval = 1
                    timerState = "Work"
                } else {
                    timer?.cancel()
                    currentInterval = 1
                    timerState = "Workout Timer"
                    backgroundColor = Color.White
                }
            }) {
                Text(if (timerState == "Workout Timer") "Start" else "Reset")
            }
        }
    }
}

fun createTimer(duration: Long, onTick: (String) -> Unit, onFinish: () -> Unit): CountDownTimer {
    return object : CountDownTimer(duration, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            val secondsLeft = millisUntilFinished / 1000
            val minutes = secondsLeft / 60
            val seconds = secondsLeft % 60
            val formattedTime = String.format("%02d:%02d", minutes, seconds)
            onTick(formattedTime)
        }

        override fun onFinish() {
            onFinish()
        }
    }
}

