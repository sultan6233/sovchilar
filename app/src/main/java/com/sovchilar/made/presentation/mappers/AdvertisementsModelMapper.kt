package com.sovchilar.made.presentation.mappers

import android.content.Context
import com.sovchilar.made.R
import sovchilar.uz.comm.andijan_region
import sovchilar.uz.comm.bukhara_region
import sovchilar.uz.comm.djizzak_region
import sovchilar.uz.comm.femaleGender
import sovchilar.uz.comm.fergana_region
import sovchilar.uz.comm.horezm_region
import sovchilar.uz.comm.karakalpak_region
import sovchilar.uz.comm.kashkadarya_region
import sovchilar.uz.comm.maleGender
import sovchilar.uz.comm.marriageDivorced
import sovchilar.uz.comm.namangan_region
import sovchilar.uz.comm.noMarriage
import sovchilar.uz.comm.novoi_region
import sovchilar.uz.comm.samarkand_region
import sovchilar.uz.comm.sirdarya_region
import sovchilar.uz.comm.surxandarya_region
import sovchilar.uz.comm.tashkent
import sovchilar.uz.comm.tashkent_region
import sovchilar.uz.comm.uzbekistan
import sovchilar.uz.comm.widower
import sovchilar.uz.domain.models.remote.AdvertisementModelPresentation
import sovchilar.uz.domain.models.remote.AdvertisementsModel
import sovchilar.uz.domain.models.states.MarriageStatus

class AdvertisementsModelMapper(val context: Context) {
    fun mapToAdvertisementModelPresentation(ad: AdvertisementsModel): AdvertisementModelPresentation {
        return AdvertisementModelPresentation(
            id = ad.id,
            name = ad.name,
            age = ad.age,
            nationality = ad.nationality,
            marriageStatus = when (ad.marriageStatus) {
                marriageDivorced -> {
                    if (ad.gender == maleGender) {
                        context.getString(R.string.marriage_status_male_divorced)
                    } else {
                        context.getString(R.string.marriage_status_female_divorced)
                    }
                }

                noMarriage -> if (ad.gender == maleGender) {
                    context.getString(R.string.marriage_status_male_no_marriage)
                } else {
                    context.getString(R.string.marriage_status_female_no_marriage)
                }

                else -> context.getString(R.string.widower)
            },
            children = when (ad.children) {
                true -> context.getString(R.string.yes_children)
                false -> context.getString(R.string.no_children)
            },
            fromAge = ad.fromAge,
            tillAge = ad.tillAge,
            telegram = ad.telegram,
            phoneNumber = ad.phoneNumber,
            city = getFixCity(ad.city),
            gender = ad.gender,
            country = getFixCountry(ad.country),
            moreInfo = ad.moreInfo
        )

    }

    private fun getFixCity(city: String): String {
        return when (city) {
            tashkent -> context.getString(R.string.tashkent)
            tashkent_region -> context.getString(R.string.tashkent_region)
            karakalpak_region -> context.getString(R.string.karakalpak_region)
            andijan_region -> context.getString(R.string.andijan_region)
            bukhara_region -> context.getString(R.string.bukhara_region)
            djizzak_region -> context.getString(R.string.djizzak_region)
            fergana_region -> context.getString(R.string.fergana_region)
            kashkadarya_region -> context.getString(R.string.kashkadarya_region)
            horezm_region -> context.getString(R.string.horezm_region)
            namangan_region -> context.getString(R.string.namangan_region)
            novoi_region -> context.getString(R.string.novoi_region)
            samarkand_region -> context.getString(R.string.samarkand_region)
            surxandarya_region -> context.getString(R.string.surxandarya_region)
            sirdarya_region -> context.getString(R.string.sirdarya_region)
            else -> ""
        }
    }

    fun mapToAdvertisementModel(advertisement: AdvertisementModelPresentation): AdvertisementsModel {
        return AdvertisementsModel(
            id = advertisement.id,
            name = advertisement.name,
            age = advertisement.age,
            nationality = advertisement.nationality,
            marriageStatus = when (advertisement.gender) {
                maleGender -> {
                    when (advertisement.marriageStatus) {
                        context.getString(R.string.marriage_status_male_divorced) -> marriageDivorced
                        context.getString(R.string.marriage_status_male_no_marriage) -> noMarriage
                        context.getString(R.string.widower) -> widower
                        else -> widower
                    }
                }

                femaleGender -> {
                    when (advertisement.marriageStatus) {
                        context.getString(R.string.marriage_status_female_divorced) -> marriageDivorced
                        context.getString(R.string.marriage_status_female_no_marriage) -> noMarriage
                        context.getString(R.string.widower) -> widower
                        else -> widower
                    }
                }

                else -> {
                    widower
                }
            },
            children = when (advertisement.children) {
                context.getString(R.string.add_have_children) -> true
                context.getString(R.string.no_children) -> false
                else -> false
            },
            fromAge = advertisement.fromAge,
            tillAge = advertisement.tillAge,
            telegram = advertisement.telegram,
            phoneNumber = advertisement.phoneNumber,
            city = getFixCityToServer(advertisement.city),
            gender = advertisement.gender,
            country = getFixCountryToServer(advertisement.country),
            moreInfo = advertisement.moreInfo
        )
    }

    private fun getFixCityToServer(city: String): String {
        return when (city) {
            context.getString(R.string.tashkent) -> tashkent
            context.getString(R.string.tashkent_region) -> tashkent_region
            context.getString(R.string.karakalpak_region) -> karakalpak_region
            context.getString(R.string.andijan_region) -> andijan_region
            context.getString(R.string.bukhara_region) -> bukhara_region
            context.getString(R.string.djizzak_region) -> djizzak_region
            context.getString(R.string.fergana_region) -> fergana_region
            context.getString(R.string.kashkadarya_region) -> kashkadarya_region
            context.getString(R.string.horezm_region) -> horezm_region
            context.getString(R.string.namangan_region) -> namangan_region
            context.getString(R.string.novoi_region) -> novoi_region
            context.getString(R.string.samarkand_region) -> samarkand_region
            context.getString(R.string.surxandarya_region) -> surxandarya_region
            context.getString(R.string.sirdarya_region) -> sirdarya_region
            else -> ""

        }
    }

    private fun getFixCountry(country: String): String {
        return when (country) {
            uzbekistan -> context.getString(R.string.uzbekistan)
            else -> context.getString(R.string.uzbekistan)
        }
    }

    private fun getFixCountryToServer(country: String): String {
        return when (country) {
            context.getString(R.string.uzbekistan) -> uzbekistan
            else -> uzbekistan
        }
    }
}