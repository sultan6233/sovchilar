package com.sovchilar.made.domain.models

data class AdvertisementsFixedModel(
    val id: Int?,
    val name: String,
    val age: Int,
    val nationality: String,
    val marriageStatus: String,
    val children: String,
    val fromAge: Int,
    val tillAge: Int,
    val telegram: String?,
    val phoneNumber: String? = null,
    val city: String,
    val gender: String,
    val country: String,
    val moreInfo: String,
)