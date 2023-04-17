package pe.com.onecanal.domain.usecases

import pe.com.onecanal.domain.entity.Either
import pe.com.onecanal.domain.entity.Failure
import pe.com.onecanal.domain.repository.AppRepository
import pe.com.onecanal.domain.usecases.base.BaseUseCase

class SaveBankAccountUseCase(private val repository: AppRepository) :
    BaseUseCase<Any, SaveBankAccountUseCase.Params>() {
    data class Params(
        val bankId: String,
        val number: String,
        val cci: String
    )

    override suspend fun run(params: Params): Either<Failure, Any> =
        repository.saveBanksAccount(
            bankId = params.bankId,
            number = params.number,
            cci = params.cci
        )
}