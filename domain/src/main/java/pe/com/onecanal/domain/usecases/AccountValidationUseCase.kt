package pe.com.onecanal.domain.usecases

import pe.com.onecanal.domain.usecases.base.BaseUseCase
import pe.com.onecanal.domain.entity.Either
import pe.com.onecanal.domain.entity.Failure
import pe.com.onecanal.domain.entity.ValidateData
import pe.com.onecanal.domain.repository.AppRepository

/**
 * Created by Sergio Carrillo Diestra on 10/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
class AccountValidationUseCase(private val appRepository: AppRepository) :
    BaseUseCase<ValidateData, AccountValidationUseCase.Params>() {

    override suspend fun run(params: Params): Either<Failure, ValidateData> {
        return appRepository.validateAccount(params.documentType, params.documentNumber)
    }

    data class Params(
        val documentType: String,
        val documentNumber: String,
    )

}