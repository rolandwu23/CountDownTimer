package com.akm.countdowntimer.model

import java.time.Duration
import java.util.concurrent.TimeUnit

data class TimerModel(
    val timeDuration: Long = 1500000,
    val status: Status = Status.STARTED,
    val toggle: ButtonState = ButtonState.START
)

enum class Status {
    STARTED, RUNNING, FINISHED
}

enum class ButtonState {
    START, PAUSE, RESUME
}