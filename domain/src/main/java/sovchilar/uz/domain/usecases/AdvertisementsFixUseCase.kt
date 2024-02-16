package sovchilar.uz.domain.usecases

//class AdvertisementsFixUseCase(val context: Context) {
//
//    fun getFixGenderDividedAdvertisements(
//        advertisementsModel: List<AdvertisementsModel>,
//        gender: String,
//    ): List<sovchilar.uz.domain.models.AdvertisementsFixedModel> {
//        val newAdvertisementsFixedModel = ArrayList<sovchilar.uz.domain.models.AdvertisementsFixedModel>()
//        getAdvertisements(advertisementsModel).forEach {
//            if (gender == setGender(it.gender)) {
//                newAdvertisementsFixedModel.add(it)
//            }
//        }
//        return newAdvertisementsFixedModel
//    }
//
//    private fun getAdvertisements(advertisementsModel: List<AdvertisementsModel>): List<sovchilar.uz.domain.models.AdvertisementsFixedModel> {
//        val fixedAdvertisementsModel = ArrayList<sovchilar.uz.domain.models.AdvertisementsFixedModel>()
//        advertisementsModel.forEach {
//            fixedAdvertisementsModel.add(
//                sovchilar.uz.domain.models.AdvertisementsFixedModel(
//                    it.id,
//                    it.name,
//                    it.age,
//                    it.nationality,
//                    getFixMarriageStatus(it.marriageStatus, it.gender),
//                    fixChildren(it.children),
//                    it.fromAge,
//                    it.tillAge,
//                    it.telegram,
//                    it.phoneNumber,
//                    getFixCity(it.city),
//                    getFixGender(it.gender),
//                    getFixCountry(it.country),
//                    it.moreInfo
//                )
//            )
//        }
//        return fixedAdvertisementsModel
//    }
//
//    fun getAdvertisementsToServer(advertisementsFixedModel: sovchilar.uz.domain.models.AdvertisementsFixedModel): AdvertisementsModel {
//        return AdvertisementsModel(
//            null,
//            advertisementsFixedModel.name,
//            advertisementsFixedModel.age,
//            advertisementsFixedModel.nationality,
//            getFixMarriageStatusToServer(advertisementsFixedModel.marriageStatus),
//            fixChildrenToServer(advertisementsFixedModel.children),
//            advertisementsFixedModel.fromAge,
//            advertisementsFixedModel.tillAge,
//            advertisementsFixedModel.telegram,
//            advertisementsFixedModel.phoneNumber,
//            getFixCityToServer(advertisementsFixedModel.city),
//            getFixGenderToServer(advertisementsFixedModel.gender),
//            getFixCountryToServer(advertisementsFixedModel.country),
//            advertisementsFixedModel.moreInfo
//        )
//    }
//
//    private fun getFixGender(gender: String): String {
//        return if (gender == maleGender) {
//            context.getString(R.string.male)
//        } else {
//            context.getString(R.string.female)
//        }
//    }
//
//    private fun getFixGenderToServer(gender: String): String {
//        return if (gender == context.getString(R.string.male)) {
//            maleGender
//        } else {
//            femaleGender
//        }
//    }
//
//    private fun getFixMarriageStatus(marriageStatus: String, gender: String): String {
//        return if (marriageStatus == marriageDivorced) {
//            if (gender == maleGender) {
//                context.getString(R.string.marriage_status_male_divorced)
//            } else {
//                context.getString(R.string.marriage_status_female_divorced)
//            }
//        } else {
//            if (gender == maleGender) {
//                context.getString(R.string.marriage_status_male_no_marriage)
//            } else {
//                context.getString(R.string.marriage_status_female_no_marriage)
//            }
//        }
//    }
//
//    private fun getFixMarriageStatusToServer(marriageStatus: String): String {
//        return if (context.getString(R.string.divorced) == marriageStatus) {
//            marriageDivorced
//        } else {
//            noMarriage
//        }
//    }
//
//    private fun fixChildren(children: Boolean): String {
//        return if (children) {
//            context.getString(R.string.add_have_children)
//        } else {
//            context.getString(R.string.add_no_children)
//        }
//    }
//
//    private fun fixChildrenToServer(children: String): Boolean {
//        return when (children) {
//            context.getString(R.string.add_have_children) -> true
//            context.getString(R.string.add_no_children) -> false
//            else -> false
//        }
//    }
//
//    private fun getFixCity(city: String): String {
//        return when (city) {
//            tashkent -> context.getString(R.string.tashkent)
//            tashkent_region -> context.getString(R.string.tashkent_region)
//            karakalpak_region -> context.getString(R.string.karakalpak_region)
//            andijan_region -> context.getString(R.string.andijan_region)
//            bukhara_region -> context.getString(R.string.bukhara_region)
//            djizzak_region -> context.getString(R.string.djizzak_region)
//            fergana_region -> context.getString(R.string.fergana_region)
//            kashkadarya_region -> context.getString(R.string.kashkadarya_region)
//            horezm_region -> context.getString(R.string.horezm_region)
//            namangan_region -> context.getString(R.string.namangan_region)
//            novoi_region -> context.getString(R.string.novoi_region)
//            samarkand_region -> context.getString(R.string.samarkand_region)
//            surxandarya_region -> context.getString(R.string.surxandarya_region)
//            sirdarya_region -> context.getString(R.string.sirdarya_region)
//            else -> ""
//        }
//    }
//
//    private fun getFixCityToServer(city: String): String {
//        return when (city) {
//            context.getString(R.string.tashkent) -> tashkent
//            context.getString(R.string.tashkent_region) -> tashkent_region
//            context.getString(R.string.karakalpak_region) -> karakalpak_region
//            context.getString(R.string.andijan_region) -> andijan_region
//            context.getString(R.string.bukhara_region) -> bukhara_region
//            context.getString(R.string.djizzak_region) -> djizzak_region
//            context.getString(R.string.fergana_region) -> fergana_region
//            context.getString(R.string.kashkadarya_region) -> kashkadarya_region
//            context.getString(R.string.horezm_region) -> horezm_region
//            context.getString(R.string.namangan_region) -> namangan_region
//            context.getString(R.string.novoi_region) -> novoi_region
//            context.getString(R.string.samarkand_region) -> samarkand_region
//            context.getString(R.string.surxandarya_region) -> surxandarya_region
//            context.getString(R.string.sirdarya_region) -> sirdarya_region
//            else -> ""
//
//        }
//    }
//
//    private fun getFixCountry(country: String): String {
//        return when (country) {
//            uzbekistan -> context.getString(R.string.uzbekistan)
//            else -> context.getString(R.string.uzbekistan)
//        }
//    }
//
//    private fun getFixCountryToServer(country: String): String {
//        return when (country) {
//            context.getString(R.string.uzbekistan) -> uzbekistan
//            else -> uzbekistan
//        }
//    }
//
//    private fun setGender(gender: String): String {
//        return when (gender) {
//            context.getString(R.string.male) -> maleGender
//            context.getString(R.string.female) -> femaleGender
//            else -> ""
//        }
//    }
//
////    private fun setGenderToServer(gender: String):String {
////        return when(gender){
////
////        }
////    }
//}