package com.sovchilar.made.core

import android.app.Application
import android.content.Context
import com.akexorcist.localizationactivity.ui.LocalizationApplication
import com.github.terrakok.cicerone.Cicerone
import com.onesignal.OneSignal
import dagger.hilt.android.HiltAndroidApp
import java.util.Locale

@HiltAndroidApp
class MainApplication : LocalizationApplication() {
    private val cicerone = Cicerone.create()
    val router get() = cicerone.router
    val navigatorHolder get() = cicerone.getNavigatorHolder()
    override fun getDefaultLanguage(context: Context) = Locale.ENGLISH

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        oneSignal()
    }

    private fun oneSignal() {
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
        OneSignal.initWithContext(this)
        OneSignal.setAppId("")
        OneSignal.promptForPushNotifications()
    }

    companion object {
        internal lateinit var INSTANCE: MainApplication
            private set
    }
}