package com.sovchilar.made.domain.usecases

import android.content.Context
import com.sovchilar.made.R
import com.sovchilar.made.domain.models.AdvertisementsFixedModel
import com.sovchilar.made.domain.models.AdvertisementsModel
import com.sovchilar.made.uitls.andijan_region
import com.sovchilar.made.uitls.bukhara_region
import com.sovchilar.made.uitls.djizzak_region
import com.sovchilar.made.uitls.femaleGender
import com.sovchilar.made.uitls.fergana_region
import com.sovchilar.made.uitls.horezm_region
import com.sovchilar.made.uitls.karakalpak_region
import com.sovchilar.made.uitls.kashkadarya_region
import com.sovchilar.made.uitls.maleGender
import com.sovchilar.made.uitls.marriageDivorced
import com.sovchilar.made.uitls.namangan_region
import com.sovchilar.made.uitls.novoi_region
import com.sovchilar.made.uitls.samarkand_region
import com.sovchilar.made.uitls.sirdarya_region
import com.sovchilar.made.uitls.surxandarya_region
import com.sovchilar.made.uitls.tashkent
import com.sovchilar.made.uitls.tashkent_region
import com.sovchilar.made.uitls.uzbekistan

class AdvertisementsFixUseCase(val context: Context) {

    fun getFixGenderDividedAdvertisements(
        advertisementsModel: List<AdvertisementsModel>,
        gender: String,
    ): List<AdvertisementsFixedModel> {
        val newAdvertisementsFixedModel = ArrayList<AdvertisementsFixedModel>()
        getAdvertisements(advertisementsModel).forEach {
            if (gender == setGender(it.gender)) {
                newAdvertisementsFixedModel.add(it)
            }
        }
        return newAdvertisementsFixedModel
    }

    private fun getAdvertisements(advertisementsModel: List<AdvertisementsModel>): List<AdvertisementsFixedModel> {
        val fixedAdvertisementsModel = ArrayList<AdvertisementsFixedModel>()
        advertisementsModel.forEach {
            fixedAdvertisementsModel.add(
                AdvertisementsFixedModel(
                    it.id,
                    it.name,
                    it.age,
                    it.nationality,
                    getFixMarriageStatus(it.marriageStatus, it.gender),
                    fixChildren(it.children),
                    it.fromAge,
                    it.tillAge,
                    it.telegram,
                    it.phoneNumber,
                    getFixCity(it.city),
                    getFixGender(it.gender),
                    getFixCountry(it.country),
                    it.moreInfo
                )
            )
        }
        return fixedAdvertisementsModel
    }

    private fun getFixGender(gender: String): String {
        return if (gender == maleGender) {
            context.getString(R.string.male)
        } else {
            context.getString(R.string.female)
        }
    }

    private fun getFixMarriageStatus(marriageStatus: String, gender: String): String {
        return if (marriageStatus == marriageDivorced) {
            if (gender == maleGender) {
                context.getString(R.string.marriage_status_male_divorced)
            } else {
                context.getString(R.string.marriage_status_female_divorced)
            }
        } else {
            if (gender == maleGender) {
                context.getString(R.string.marriage_status_male_no_marriage)
            } else {
                context.getString(R.string.marriage_status_female_no_marriage)
            }
        }
    }

    private fun fixChildren(children: Boolean): String {
        return if (children) {
            context.getString(R.string.have_children)
        } else {
            context.getString(R.string.no_children)
        }
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

    private fun getFixCountry(country: String): String {
        return when (country) {
            uzbekistan -> context.getString(R.string.uzbekistan)
            else -> context.getString(R.string.uzbekistan)
        }
    }

    private fun setGender(gender: String): String {
        return when (gender) {
            context.getString(R.string.male) -> maleGender
            context.getString(R.string.female) -> femaleGender
            else -> ""
        }
    }
}