package com.sovchilar.made

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import io.appmetrica.analytics.AdRevenue
import io.appmetrica.analytics.AdType
import io.appmetrica.analytics.AppMetrica
import java.math.BigDecimal
import java.util.Currency


object CustomLogger {
    private val firebaseAnalytics: FirebaseAnalytics by lazy { Firebase.analytics }
    fun log(name: String, value: String) {
        val bundle = Bundle()
        bundle.putString(value, value)
        firebaseAnalytics.logEvent(name, bundle)
        val newEventParameters: MutableMap<String, Any> = HashMap()
        newEventParameters[name] = value
        AppMetrica.reportEvent(name, newEventParameters)
    }

    fun logAdRevenue(
        revenue: Double,
        adNetwork: String,
        placementId: String,
        placementName: String,
        adType: AdType,
        adUnitId: String,
        adUnitName: String,
        precision: String
    ) {
        val adRevenuePayload: MutableMap<String, String> = HashMap()
        adRevenuePayload["payload_key_1"] = "payload_value_1"
        val adRevenue: AdRevenue =
            AdRevenue.newBuilder(BigDecimal(revenue), Currency.getInstance("USD"))
                .withAdNetwork("ad_network")
                .withAdPlacementId("ad_placement_id")
                .withAdPlacementName("ad_placement_name")
                .withAdType(AdType.NATIVE)
                .withAdUnitId("ad_unit_id")
                .withAdUnitName("ad_unit_name")
                .withPrecision("some precision")
                .withPayload(adRevenuePayload)
                .build()
    }
}