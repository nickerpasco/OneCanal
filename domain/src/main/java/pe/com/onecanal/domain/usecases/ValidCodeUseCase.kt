package pe.com.onecanal.domain.usecases

import pe.com.onecanal.domain.entity.Either
import pe.com.onecanal.domain.entity.Failure
import pe.com.onecanal.domain.repository.AppRepository
import pe.com.onecanal.domain.usecases.base.BaseUseCase

class ValidCodeUseCase(private val appRepository: AppRepository) :
    BaseUseCase<Any, ValidCodeUseCase.Params>() {

    override suspend fun run(params: Params): Either<Failure, Any> {
        return appRepository.validateToken(
            params.code,
            params.email,
            params.user_Id
        )
    }

    data class Params(
        val code: String,
        val email: String,
        val user_Id: Int
    )
}