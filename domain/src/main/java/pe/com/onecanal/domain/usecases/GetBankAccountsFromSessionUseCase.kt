package pe.com.onecanal.domain.usecases

import pe.com.onecanal.domain.entity.BankAccount
import pe.com.onecanal.domain.entity.Either
import pe.com.onecanal.domain.entity.Failure
import pe.com.onecanal.domain.entity.SalaryAdvanceReason
import pe.com.onecanal.domain.repository.AppRepository
import pe.com.onecanal.domain.usecases.base.BaseUseCaseNoParams

/**
 * Created by Sergio Carrillo Diestra on 18/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
class GetBankAccountsFromSessionUseCase(private val repository: AppRepository): BaseUseCaseNoParams<List<BankAccount>>() {
    override suspend fun run(): Either<Failure, List<BankAccount>> {
        return repository.getBankAccountsFromSession()
    }
}