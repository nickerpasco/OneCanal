package pe.com.onecanal.domain.usecases

import pe.com.onecanal.domain.usecases.base.BaseUseCase
import pe.com.onecanal.domain.entity.Either
import pe.com.onecanal.domain.entity.Failure
import pe.com.onecanal.domain.entity.ValidateData
import pe.com.onecanal.domain.repository.AppRepository

class ForgotPasswordUseCase(private val repository: AppRepository) :
    BaseUseCase<ValidateData, ForgotPasswordUseCase.Params>() {
    data class Params(
        val documentType: String,
        val documentNumer: String
    )

    override suspend fun run(params: Params): Either<Failure, ValidateData> =
        repository.forgotPassword(params.documentType, params.documentNumer)
}
