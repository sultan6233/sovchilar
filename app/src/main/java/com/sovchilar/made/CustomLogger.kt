package com.sovchilar.made

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import io.appmetrica.analytics.AppMetrica

object CustomLogger {
    private val firebaseAnalytics: FirebaseAnalytics by lazy { Firebase.analytics }
    fun log(name: String, value: String) {
        val bundle = Bundle()
        bundle.putString(value, value)
        firebaseAnalytics.logEvent(name, bundle)
        AppMetrica.reportEvent(name, value)
    }
}