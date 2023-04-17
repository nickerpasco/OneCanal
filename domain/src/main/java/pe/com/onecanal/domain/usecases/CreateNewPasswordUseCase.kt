package pe.com.onecanal.domain.usecases

import pe.com.onecanal.domain.usecases.base.BaseUseCase
import pe.com.onecanal.domain.entity.Either
import pe.com.onecanal.domain.entity.Failure
import pe.com.onecanal.domain.repository.AppRepository

class CreateNewPasswordUseCase(private val appRepository: AppRepository) :
    BaseUseCase<Any, CreateNewPasswordUseCase.Params>() {

    override suspend fun run(params: Params): Either<Failure, Any> {
        return appRepository.createNewPassword(
            params.code,
            params.email,
            params.user_Id,
            params.password,
            params.password_confirmation
        )
    }

    data class Params(
        val code: String,
        val email: String,
        val user_Id: Int,
        val password: String,
        val password_confirmation: String
    )
}
