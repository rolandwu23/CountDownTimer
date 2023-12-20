package com.akm.countdowntimer.ui.theme

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorProducer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akm.countdowntimer.R
import com.akm.countdowntimer.model.ButtonState
import com.akm.countdowntimer.model.TimerModel
import com.akm.countdowntimer.model.TimerViewModel
import com.akm.countdowntimer.ui.ext.format
import kotlin.time.toJavaDuration

@ExperimentalAnimationApi
@Composable
fun TimerHomeScreen(viewModel: TimerViewModel) {
    val timer by viewModel.viewState.collectAsState(TimerModel())
    val time = remember {
        derivedStateOf { timer.timeDuration.format() }
    }
    val remainingTime = remember {
        derivedStateOf { timer.timeDuration }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.Black),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TimerHeader()
        Spacer(modifier = Modifier.height(25.dp))
        TimerTopSection(
            { time.value },
            { remainingTime.value }
        )
        Spacer(modifier = Modifier.height(25.dp))
        TimerButtons(viewModel)
    }
}

@Composable
fun TimerTopSection(
    time: () -> String,
    remainingTime: () -> Long
) {
//    val infiniteTransition = rememberInfiniteTransition(label = "CountDownColor")
//    val alpha by infiniteTransition.animateColor(
//        initialValue = Color.Red,
//        targetValue = Color.Green,
//        animationSpec = infiniteRepeatable(
//            animation = tween(1000, easing = LinearEasing),
//            repeatMode = RepeatMode.Reverse
//        ),
//        label = "CountDownColorAlpha"
//    )
//    val color = remember {
//        derivedStateOf {
//            if (isTimeLessThan10Seconds(remainingTime())) alpha else Color.White
//        }
//    }
    val color by animateColorBetween(remainingTime)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicText(
            text = time(),
            style = TextStyle(fontSize = 60.sp),
            color = { color }
        )
    }
}


@Composable
private fun animateColorBetween(
    remainingTime: () -> Long
): State<Color> {
    val infiniteTransition = rememberInfiniteTransition(label = "CountDownColor")
    val alpha by infiniteTransition.animateColor(
        initialValue = Color.Red,
        targetValue = Color.Green,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "CountDownColorAlpha"
    )
    val color = remember {
        derivedStateOf {
            if (isTimeLessThan10Seconds(remainingTime())) alpha else Color.White
        }
    }
    return color
}

@ExperimentalAnimationApi
@Composable
fun TimerButtons(viewModel: TimerViewModel) {
    val timer by viewModel.viewState.collectAsState(TimerModel())
    val toggle = remember {
        derivedStateOf { timer.toggle }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            viewModel.resetTimer()
        }) {
            Icon(painter = painterResource(R.drawable.ic_stop), contentDescription = "stop button")
        }

        ButtonLayout(
            { toggle.value },
            { viewModel.resetTimer() },
            { viewModel.buttonSelection() }
        )
    }
}

@Composable
fun ButtonLayout(
    toggle: () -> ButtonState,
    onResetClick: () -> Unit,
    onButtonClick: () -> Unit
) {
    var text = ""
    var color: Color = MaterialTheme.colors.primaryVariant
    var textColor: Color = Color.White
    when (toggle()) {
        ButtonState.START -> {
            text = "Start"
            color = MaterialTheme.colors.primaryVariant
            textColor = Color.White
        }
        ButtonState.PAUSE -> {
            text = "Pause"
            color = MaterialTheme.colors.secondary
            textColor = Color.Black
        }
        ButtonState.RESUME -> {
            text = "Resume"
            color = MaterialTheme.colors.secondaryVariant
            textColor = Color.Black
        }

        else -> {}
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(40.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(modifier = Modifier
            .clickable {
                onResetClick()
            }
            .padding(30.dp)
            .size(80.dp)
            .clip(CircleShape)
            .background(Color.DarkGray)
            .fillMaxWidth()) {
            Text(
                text = "Reset", color = Color.White, modifier = Modifier
                    .align(Alignment.Center)
                    .padding(8.dp)
            )
        }

        Box(modifier = Modifier
            .clickable {
                onButtonClick()
            }
            .padding(10.dp)
            .size(80.dp)
            .clip(CircleShape)
            .background(color)
            .fillMaxWidth()) {
            Text(
                text = text, color = textColor, modifier = Modifier
                    .align(Alignment.Center)
                    .padding(8.dp)
            )
        }
    }
}

@Composable
fun TimerHeader() {
    Text(
        text = "Count Down Timer",
        fontSize = 30.sp,
        color = Color.White,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 80.dp),
        style = MaterialTheme.typography.h4
    )
}

private fun isTimeLessThan10Seconds(time: Long) = time < 10000L