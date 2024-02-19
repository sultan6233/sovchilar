package sovchilar.uz.domain.usecases

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import sovchilar.uz.domain.IAdvertisement
import sovchilar.uz.domain.models.PostResponse
import sovchilar.uz.domain.models.UserModel
import sovchilar.uz.domain.models.remote.AdvertisementsModel
import sovchilar.uz.domain.utils.DataState
import javax.inject.Inject

class GetAdvertisementsUseCase @Inject constructor(private val advertisementRepository: IAdvertisement) {
    suspend operator fun invoke(): Flow<PagingData<AdvertisementsModel>> {
        return advertisementRepository.getAdvertisements()
    }
}