package com.sovchilar.made.domain.models

data class AdvertisementsModel(
    val id:Int?,
    val name:String,
    val age:Int,
    val nationality:String,
    val marriagestatus:String,
    val children:Boolean,
    val fromAge:Int,
    val tillAge:Int,
    val telegram:String?,
    val phoneNumber: String?,
    val city:String,
    val gender:String,
    val country:String,
    val moreInfo:String)