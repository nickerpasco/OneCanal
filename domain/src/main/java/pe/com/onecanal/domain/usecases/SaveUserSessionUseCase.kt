package pe.com.onecanal.domain.usecases

import pe.com.onecanal.domain.entity.Either
import pe.com.onecanal.domain.entity.Failure
import pe.com.onecanal.domain.entity.UserProfileData
import pe.com.onecanal.domain.repository.AppRepository
import pe.com.onecanal.domain.usecases.base.BaseUseCase

/**
 * Created by Sergio Carrillo Diestra on 18/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
class SaveUserSessionUseCase(private val repository: AppRepository) :
    BaseUseCase<Unit, UserProfileData>() {

    override suspend fun run(params: UserProfileData): Either<Failure, Unit> {
        return repository.saveUserSession(params)
    }
}