package com.akm.countdowntimer.model

import android.os.Build
import android.os.CountDownTimer
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.Duration

class TimerViewModel : ViewModel() {

    private val _viewState = MutableLiveData<TimerModel>()
    val viewState: LiveData<TimerModel> = _viewState

    var countDown: CountDownTimer? = null

    init {
        _viewState.value = TimerModel()
    }

    fun startTime(duration: Duration) {
        countDown = object : CountDownTimer(duration.toMillis(), 1000) {
            override fun onTick(seconds: Long) {
                _viewState.value = TimerModel(
                    timeDuration = Duration.ofMillis(seconds),
                    remainingTime = seconds,
                    status = Status.RUNNING,
                    toggle = ButtonState.PAUSE
                )
            }

            override fun onFinish() {
                _viewState.value = _viewState.value!!.copy(
                    timeDuration = Duration.ZERO,
                    status = Status.FINISHED,
                    toggle = ButtonState.START
                )
            }

        }
        countDown?.start()
    }

    private fun pauseTimer() {
        countDown?.cancel()
        _viewState.value = _viewState.value!!.copy(
            status = Status.STARTED,
            toggle = ButtonState.RESUME
        )
    }

    fun resetTimer() {
        countDown?.cancel()
        _viewState.value = _viewState.value!!.copy(
            status = Status.STARTED,
            timeDuration = Duration.ofMillis(30000),
            toggle = ButtonState.START
        )
    }

    fun buttonSelection() {
        val state = _viewState.value

        when (state?.status) {
            Status.STARTED -> {
                startTime(state.timeDuration)
            }
            Status.RUNNING -> {
                pauseTimer()
            }
            Status.FINISHED -> {
                resetTimer()
            }

            else -> {}
        }

    }
}