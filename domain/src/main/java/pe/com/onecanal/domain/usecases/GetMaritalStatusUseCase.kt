package pe.com.onecanal.domain.usecases

import pe.com.onecanal.domain.entity.Either
import pe.com.onecanal.domain.entity.Failure
import pe.com.onecanal.domain.entity.MaritalStatus
import pe.com.onecanal.domain.repository.AppRepository
import pe.com.onecanal.domain.usecases.base.BaseUseCaseNoParams

class GetMaritalStatusUseCase(private val repository: AppRepository): BaseUseCaseNoParams<List<MaritalStatus>>() {
    override suspend fun run(): Either<Failure, List<MaritalStatus>> {
        return repository.getMaritalStatus()
    }
}