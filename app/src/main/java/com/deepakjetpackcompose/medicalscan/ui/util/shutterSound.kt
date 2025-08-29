package com.deepakjetpackcompose.medicalscan.ui.util

import android.media.MediaActionSound

val shutterSound = MediaActionSound()

fun playShutterSound() {
    shutterSound.load(MediaActionSound.SHUTTER_CLICK)
    shutterSound.play(MediaActionSound.SHUTTER_CLICK)
}
