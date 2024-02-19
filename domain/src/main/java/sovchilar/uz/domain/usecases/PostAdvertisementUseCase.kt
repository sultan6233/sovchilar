package sovchilar.uz.domain.usecases

import kotlinx.coroutines.flow.Flow
import sovchilar.uz.domain.IAdvertisement
import sovchilar.uz.domain.models.PostResponse
import sovchilar.uz.domain.models.remote.AdvertisementsModel
import sovchilar.uz.domain.utils.DataState
import javax.inject.Inject

class PostAdvertisementUseCase @Inject constructor(private val advertisementRepository: IAdvertisement) {
    suspend operator fun invoke(
        authToken: String,
        advertisement: AdvertisementsModel
    ): Flow<DataState<PostResponse>> {
        return advertisementRepository.postAdvertisement(authToken, advertisement)
    }
}