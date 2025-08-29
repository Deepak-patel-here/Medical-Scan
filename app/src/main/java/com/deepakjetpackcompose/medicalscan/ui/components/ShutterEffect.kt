package com.deepakjetpackcompose.medicalscan.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay

@Composable
fun ShutterEffect(trigger: Boolean, onAnimationEnd: () -> Unit) {
    if (trigger) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .alpha(0.8f) // brightness
        )
        LaunchedEffect(Unit) {
            delay(100) // flash duration
            onAnimationEnd()
        }
    }
}



