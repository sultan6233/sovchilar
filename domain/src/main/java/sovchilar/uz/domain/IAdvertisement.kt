package sovchilar.uz.domain

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import sovchilar.uz.domain.models.PostResponse
import sovchilar.uz.domain.models.UserModel
import sovchilar.uz.domain.models.remote.AdvertisementsModel
import sovchilar.uz.domain.models.remote.auth.AuthModel
import sovchilar.uz.domain.models.remote.auth.AuthResponseModel
import sovchilar.uz.domain.models.remote.auth.AuthState
import sovchilar.uz.domain.utils.DataState

interface IAdvertisement {
    suspend fun postAdvertisement(authToken: String, advertisement: AdvertisementsModel): Flow<DataState<PostResponse>>
    suspend fun getAdvertisements(): Flow<PagingData<AdvertisementsModel>>
    suspend fun registerUser(authModel: AuthModel): Flow<AuthState>
}