package pe.com.onecanal.domain.usecases

import pe.com.onecanal.domain.usecases.base.BaseUseCase
import pe.com.onecanal.domain.entity.Either
import pe.com.onecanal.domain.entity.Failure
import pe.com.onecanal.domain.entity.UserProfileData
import pe.com.onecanal.domain.repository.AppRepository

/**
 * Created by Sergio Carrillo Diestra on 10/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
class DoLoginUseCase(private val appRepository: AppRepository) :
    BaseUseCase<UserProfileData, DoLoginUseCase.Params>() {

    override suspend fun run(params: Params): Either<Failure, UserProfileData> {
        return appRepository.doLogin(params.documentType, params.documentNumber, params.password)
    }

    data class Params(
        val documentType: String,
        val documentNumber: String,
        val password: String
    )

}