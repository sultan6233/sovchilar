package sovchilar.arch.featureremoteapi

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import sovchilar.uz.domain.IAdvertisement
import sovchilar.uz.domain.models.PostResponse
import sovchilar.uz.domain.models.UserModel
import sovchilar.uz.domain.models.remote.AdvertisementsModel
import sovchilar.uz.domain.utils.DataState
import javax.inject.Inject


class RemoteAdvertisement @Inject constructor(private val apiService: ApiService) : IAdvertisement {
    override suspend fun postAdvertisement(
        authToken: String,
        advertisement: AdvertisementsModel
    ): Flow<DataState<PostResponse>> = flow {
        emit(DataState.Loading)
        try {
            val response = apiService.postAdvertisement(authToken, advertisement)
            if (response.isSuccessful && response.body() != null) {
                emit(DataState.Success(response.body()!!))
            } else {
                emit(DataState.Error(Exception("Error posting advertisement")))
            }
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }

    override suspend fun getAdvertisements(): Flow<DataState<UserModel>> =
        flow {
            emit(DataState.Loading)
            try {
                val response = apiService.getAdvertisements()
                if (response.isSuccessful && response.body() != null) {
                    emit(DataState.Success(response.body()!!))
                } else {
                    emit(DataState.Error(Exception("Error fetching advertisements")))
                }
            } catch (e: Exception) {
                emit(DataState.Error(e))
            }
        }
}