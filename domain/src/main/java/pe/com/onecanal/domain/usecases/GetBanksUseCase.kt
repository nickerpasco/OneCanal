package pe.com.onecanal.domain.usecases

import pe.com.onecanal.domain.entity.AccountBank
import pe.com.onecanal.domain.entity.Either
import pe.com.onecanal.domain.entity.Failure
import pe.com.onecanal.domain.repository.AppRepository
import pe.com.onecanal.domain.usecases.base.BaseUseCaseNoParams

class GetBanksUseCase(private val repository: AppRepository): BaseUseCaseNoParams<List<AccountBank>>() {
    override suspend fun run(): Either<Failure, List<AccountBank>> {
        return repository.getBanks()
    }
}