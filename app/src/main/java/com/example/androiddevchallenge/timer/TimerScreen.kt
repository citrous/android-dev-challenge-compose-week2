/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.timer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge.ui.theme.typography

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TimerScreen(viewModel: TimerViewModel) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(color = MaterialTheme.colors.background)
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.width(40.dp))
        Row {
            TextButton(stringResource(id = R.string.add_1_min)) { viewModel.add1min() }
            TextButton(stringResource(id = R.string.add_1_sec)) { viewModel.add1sec() }
        }
        TextButton(stringResource(id = R.string.reset), MaterialTheme.colors.secondary) {
            viewModel.reset()
        }
        Spacer(modifier = Modifier.width(16.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = { viewModel.onToggleTimer() })
        ) {
            DigitBox(viewModel.minutes2, viewModel.isRunning)
            DigitBox(viewModel.minutes1, viewModel.isRunning)
            Text(
                text = ":",
                style = typography.h1,
                color = MaterialTheme.colors.onBackground
            )
            DigitBox(viewModel.seconds2, viewModel.isRunning)
            DigitBox(viewModel.seconds1, viewModel.isRunning)
        }
    }
}

@Composable
fun TextButton(
    buttonText: String,
    color: Color = MaterialTheme.colors.primary,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = buttonText,
            style = typography.button,
            modifier = Modifier
                .background(
                    color = color,
                    shape = RoundedCornerShape(4.dp)
                )
                .clickable { onClick() }
                .padding(8.dp)
        )
    }
}

@Composable
fun DigitBox(digit: Int, isRunning: Boolean) {
    var isEven by remember { mutableStateOf(digit % 2 == 0) }
    isEven = digit % 2 == 0
    val previous = if (isRunning) digit + 1 else digit - 1
    val lastDigit = if (previous > 9 || previous < 0) 0 else previous
    Box {
        TimerText(digit = if (isEven) digit else lastDigit, shown = isEven)
        TimerText(digit = if (isEven) lastDigit else digit, shown = !isEven)
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TimerText(
    digit: Int,
    shown: Boolean
) {
    AnimatedVisibility(
        visible = shown,
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> -fullHeight },
            animationSpec = spring(dampingRatio = 0.5f)
        ),
        exit = fadeOut(
            animationSpec = tween(durationMillis = 200, easing = FastOutLinearInEasing)
        )
    ) {
        Text(
            text = digit.toString(),
            style = typography.h1,
            color = MaterialTheme.colors.onBackground,
        )
    }
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun PreviewTimerLightScreen() {
    MyTheme {
        TimerScreen(TimerViewModel())
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun PreviewTimerDarkScreen() {
    MyTheme(darkTheme = true) {
        TimerScreen(TimerViewModel())
    }
}
