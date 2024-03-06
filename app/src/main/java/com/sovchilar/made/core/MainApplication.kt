package com.sovchilar.made.core

import android.content.Context
import com.akexorcist.localizationactivity.ui.LocalizationApplication
import com.github.terrakok.cicerone.Cicerone
import com.onesignal.OneSignal
import dagger.hilt.android.HiltAndroidApp
import io.appmetrica.analytics.AppMetrica
import io.appmetrica.analytics.AppMetricaConfig
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
        initAppmetrica()
    }

    private fun initAppmetrica() {
        val config =
            AppMetricaConfig.newConfigBuilder("0992fcff-436d-42af-aa41-be9eb93d6926").build()
        AppMetrica.activate(this, config)
        AppMetrica.enableActivityAutoTracking(this)
    }

    private fun oneSignal() {
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
        OneSignal.initWithContext(this)
        OneSignal.setAppId("19c2dbbf-5cc0-4fde-b6d2-7a050280b9a6")
        OneSignal.promptForPushNotifications()
    }

    companion object {
        internal lateinit var INSTANCE: MainApplication
            private set
    }
}