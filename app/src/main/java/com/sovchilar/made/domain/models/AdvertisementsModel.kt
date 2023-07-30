package com.sovchilar.made.domain.models

import com.google.gson.annotations.SerializedName

data class AdvertisementsModel(
    val id: Int? = null,
    val name: String,
    val age: Int,
    val nationality: String,
    val marriageStatus: String,
    val children: Boolean,
    val fromAge: Int,
    val tillAge: Int,
    val telegram: String?,
    val phoneNumber: String? = null,
    val city: String,
    val gender: String,
    val country: String,
    val moreInfo: String,
)