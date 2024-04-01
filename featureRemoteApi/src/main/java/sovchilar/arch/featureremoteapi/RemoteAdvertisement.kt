package sovchilar.arch.featureremoteapi

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import retrofit2.Converter
import sovchilar.uz.domain.IAdvertisement
import sovchilar.uz.domain.models.PostResponse
import sovchilar.uz.domain.models.UserModel
import sovchilar.uz.domain.models.remote.AdvertisementsModel
import sovchilar.uz.domain.models.remote.auth.AuthErrorModel
import sovchilar.uz.domain.models.remote.auth.AuthModel
import sovchilar.uz.domain.models.remote.auth.AuthState
import sovchilar.uz.domain.utils.DataState
import javax.inject.Inject


class RemoteAdvertisement @Inject constructor(private val apiService: ApiService) : IAdvertisement {
    override suspend fun postAdvertisement(
        authToken: String,
        advertisement: AdvertisementsModel
    ): Flow<DataState<PostResponse>> = flow {
        emit(DataState.Loading)
        try {
            val response = apiService.postAdvertisement("Bearer $authToken", advertisement)
            if (response.isSuccessful && response.body() != null) {
                emit(DataState.Success(response.body()!!))
            } else {
                emit(DataState.Error(Exception("Error posting advertisement")))
            }
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }

    override suspend fun getAdvertisements(gender: String): Flow<PagingData<AdvertisementsModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { AdvertisementPagingSource(apiService, gender, true) }
        ).flow
    }

    override suspend fun registerUser(authModel: AuthModel): Flow<AuthState> = flow {
        emit(AuthState.Loading)
        try {
            val response = apiService.loginOrRegister(authModel)
            if (response.isSuccessful && response.body() != null) {
                emit(AuthState.AUTHENTICATED(response.body()!!))
            } else {
                response.errorBody()?.let { errorBody ->
                    val authError = Gson().fromJson(errorBody.string(), AuthErrorModel::class.java)
                    emit(AuthState.INVALID_AUTHENTICATION(authError.message))
                } ?: emit(AuthState.INVALID_AUTHENTICATION("Unknown Error"))

            }
        } catch (e: Exception) {
            emit(AuthState.CONNECTION_ERROR(e.message.toString()))
        }
    }
}