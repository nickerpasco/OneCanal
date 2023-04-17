package pe.com.onecanal.domain.usecases

import pe.com.onecanal.domain.entity.Either
import pe.com.onecanal.domain.entity.Failure
import pe.com.onecanal.domain.entity.SalaryAdvanceDetails
import pe.com.onecanal.domain.repository.AppRepository
import pe.com.onecanal.domain.usecases.base.BaseUseCase

class GetSalaryAdvanceDetailsUseCase(private val repository: AppRepository) :
    BaseUseCase<SalaryAdvanceDetails, GetSalaryAdvanceDetailsUseCase.Params>() {
    data class Params(
        val amount: Double,
    )

    override suspend fun run(params: Params): Either<Failure, SalaryAdvanceDetails> =
        repository.getSalaryAdvanceDetails(params.amount)
}
