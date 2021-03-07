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

import android.media.AudioManager
import android.media.ToneGenerator
import android.os.CountDownTimer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class TimerViewModel : ViewModel() {

    private var remainingSeconds: Long by mutableStateOf(0)

    val minutes2: Int
        get() = minutes() / 10

    val minutes1: Int
        get() = minutes() % 10

    val seconds2: Int
        get() = seconds() / 10

    val seconds1: Int
        get() = seconds() % 10

    var isRunning: Boolean = false
        private set

    private var countDownTimer: CountDownTimer? = null

    private fun startTimer(millis: Long) {
        countDownTimer = object : CountDownTimer(millis, CountDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                if (isRunning) {
                    remainingSeconds = millisUntilFinished / 1000
                }
            }

            override fun onFinish() {
                countDownTimer = null
                isRunning = false
                val toneGenerator =
                    ToneGenerator(AudioManager.STREAM_SYSTEM, 50)
                toneGenerator.startTone(ToneGenerator.TONE_CDMA_MED_L, BeepDuration)
            }
        }
        countDownTimer?.start()
    }

    private fun minutes(): Int {
        return (remainingSeconds / 60).toInt()
    }

    private fun seconds(): Int {
        return (remainingSeconds % 60).toInt()
    }

    fun onToggleTimer() {
        isRunning = if (countDownTimer == null) {
            startTimer(remainingSeconds * 1000)
            true
        } else {
            countDownTimer?.cancel()
            countDownTimer = null
            false
        }
    }

    fun add1sec() {
        if (!isRunning) {
            remainingSeconds += 1
        }
    }

    fun add1min() {
        if (!isRunning) {
            remainingSeconds += 60
        }
    }

    fun reset() {
        if (isRunning) {
            countDownTimer?.cancel()
            countDownTimer = null
        }
        remainingSeconds = 0
    }

    companion object {
        private const val CountDownInterval: Long = 1000
        private const val BeepDuration: Int = 1000
    }
}
