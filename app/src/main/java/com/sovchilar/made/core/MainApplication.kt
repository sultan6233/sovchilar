package com.sovchilar.made.core

import android.app.Application
import android.os.SystemClock
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication:Application() {
    private val dexLoadTime = SystemClock.currentThreadTimeMillis()
    override fun onCreate() {
        super.onCreate()
        Log.d("suka",dexLoadTime.toString())
    }
}