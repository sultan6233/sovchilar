package sovchilar.uz.domain

import kotlinx.coroutines.flow.Flow
import sovchilar.uz.domain.models.PostResponse
import sovchilar.uz.domain.models.UserModel
import sovchilar.uz.domain.models.remote.AdvertisementsModel
import sovchilar.uz.domain.utils.DataState

interface IAdvertisement {
    suspend fun postAdvertisement(authToken: String, advertisement: AdvertisementsModel): Flow<DataState<PostResponse>>
    suspend fun getAdvertisements(): Flow<DataState<UserModel>>
}