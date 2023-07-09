package com.sovchilar.made.uitls

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import java.util.Locale


object LocaleHelper {

    private var defaultLocale: Locale? = null

    fun setLocale(context: Context, language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val resources = context.resources
        val configuration = Configuration(resources.configuration)

        configuration.setLocale(locale)

        resources.updateConfiguration(configuration, resources.displayMetrics)
    }

    fun updateConfiguration(context: Context): Context {
        val resources = context.resources
        val configuration = Configuration(resources.configuration)

        configuration.setLocale(Locale.getDefault())
        val localeList = LocaleList(Locale.getDefault())
        LocaleList.setDefault(localeList)
        configuration.setLocales(localeList)

        context.createConfigurationContext(configuration)

        return context
    }

    fun storeDefaultLocale() {
        defaultLocale = Locale.getDefault()
    }

    fun resetLocale(context: Context) {
        val resources = context.resources
        val configuration = Configuration(resources.configuration)
        configuration.setToDefaults()
        Locale.setDefault(Locale.getDefault())
        defaultLocale = Locale.getDefault()
        configuration.locale = defaultLocale

        context.createConfigurationContext(configuration)
    }
}