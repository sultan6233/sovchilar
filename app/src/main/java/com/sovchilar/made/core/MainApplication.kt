package com.sovchilar.made.core

import android.app.Application
import com.onesignal.OneSignal
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        oneSignal()
    }

    private fun oneSignal() {
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
        OneSignal.initWithContext(this)
        OneSignal.setAppId("1e954268-f6b4-4fe9-be85-e1c20c4c2f18")
        OneSignal.promptForPushNotifications()
    }
}