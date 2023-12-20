package com.akm.countdowntimer.model

import android.os.Build
import android.os.CountDownTimer
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.Duration
import java.util.concurrent.TimeUnit

class TimerViewModel : ViewModel() {

    private val _viewState = MutableStateFlow<TimerModel>(TimerModel())
    val viewState: StateFlow<TimerModel> = _viewState

    var countDown: CountDownTimer? = null

    init {
        _viewState.value = TimerModel()
    }

    fun startTime(duration: Long) {
        countDown = object : CountDownTimer(duration, 10) {
            override fun onTick(seconds: Long) {
                _viewState.value = TimerModel(
                    timeDuration = seconds,
                    status = Status.RUNNING,
                    toggle = ButtonState.PAUSE
                )
            }

            override fun onFinish() {
                _viewState.value = _viewState.value!!.copy(
                    timeDuration = 0,
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
            timeDuration = 30000,
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