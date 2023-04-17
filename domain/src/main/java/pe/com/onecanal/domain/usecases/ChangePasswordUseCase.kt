package pe.com.onecanal.domain.usecases

import pe.com.onecanal.domain.usecases.base.BaseUseCase
import pe.com.onecanal.domain.entity.Either
import pe.com.onecanal.domain.entity.Failure
import pe.com.onecanal.domain.repository.AppRepository

class ChangePasswordUseCase(private val repository: AppRepository) :
    BaseUseCase<Any, ChangePasswordUseCase.Params>() {
    data class Params(
        val oldPassword: String,
        val newPassword: String,
        val newPasswordConfirmation: String
    )

    override suspend fun run(params: Params): Either<Failure, Any> =
        repository.changePassword(params.oldPassword,params.newPassword,params.newPasswordConfirmation)
}
