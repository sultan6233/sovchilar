package com.sovchilar.made.domain.models

import com.google.gson.annotations.SerializedName

data class AdvertisementsModel(
    val id: Int?,
    val name: String,
    val age: Int,
    val nationality: String,
    @SerializedName("marriagestatus")
    val marriageStatus: String,
    val children: Boolean,
    @SerializedName("fromage")
    val fromAge: Int,
    @SerializedName("tillage")
    val tillAge: Int,
    val telegram: String?,
    @SerializedName("phonenumber")
    val phoneNumber: String?,
    val city: String,
    val gender: String,
    val country: String,
    @SerializedName("moreinfo")
    val moreInfo: String,
)