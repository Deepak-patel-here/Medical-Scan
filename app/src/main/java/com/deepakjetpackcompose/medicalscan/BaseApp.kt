package com.deepakjetpackcompose.medicalscan

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApp: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}