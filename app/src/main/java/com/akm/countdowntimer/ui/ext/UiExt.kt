package com.akm.countdowntimer.ui.ext

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Duration
import kotlin.math.abs

@RequiresApi(Build.VERSION_CODES.O)
fun Duration.format(): String {
    val seconds = abs(seconds)
    val value = String.format(
        "%02d:%02d",
        seconds % 3600 / 60,
        seconds % 60
    )
    return value
}

fun Long.format(): String {
    val seconds = abs(this) / 1000
    return String.format(
        "%02d:%02d",
        seconds % 3600 / 60,
        seconds % 60
    )
}