package pe.com.onecanal.domain.usecases

import pe.com.onecanal.domain.entity.Either
import pe.com.onecanal.domain.entity.Failure
import pe.com.onecanal.domain.repository.AppRepository
import pe.com.onecanal.domain.usecases.base.BaseUseCase

class SaveSalaryAdvanceUseCase(private val repository: AppRepository) :
    BaseUseCase<Any, SaveSalaryAdvanceUseCase.Params>() {
    data class Params(
        val account_id: Int,
        val amount: Double,
        val fees: String,
        val period_name: String,
        val reason_id: Int,
        val transfer_amount: String
    )

    override suspend fun run(params: Params): Either<Failure, Any> =
        repository.saveSalaryAdvance(
            account_id = params.account_id,
            amount = params.amount,
            fees = params.fees,
            period_name = params.period_name,
            reason_id = params.reason_id,
            transfer_amount = params.transfer_amount
        )
}
