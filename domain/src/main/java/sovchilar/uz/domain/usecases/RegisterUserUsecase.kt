package sovchilar.uz.domain.usecases

import kotlinx.coroutines.flow.Flow
import sovchilar.uz.domain.IAdvertisement
import sovchilar.uz.domain.models.remote.auth.AuthModel
import sovchilar.uz.domain.models.remote.auth.AuthState
import javax.inject.Inject

class RegisterUserUsecase @Inject constructor(private val advertisementRepository: IAdvertisement) {
    suspend operator fun invoke(authModel: AuthModel): Flow<AuthState> {
        return advertisementRepository.registerUser(authModel)
    }
}