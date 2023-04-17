package pe.com.onecanal.domain.usecases

import pe.com.onecanal.domain.entity.Either
import pe.com.onecanal.domain.entity.Failure
import pe.com.onecanal.domain.entity.HistoryPagination
import pe.com.onecanal.domain.repository.AppRepository
import pe.com.onecanal.domain.usecases.base.BaseUseCase

/**
 * Created by Sergio Carrillo Diestra on 18/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
class GetHistoryUseCase(private val repository: AppRepository) :
    BaseUseCase<HistoryPagination, GetHistoryUseCase.Params>() {
    override suspend fun run(params: Params): Either<Failure, HistoryPagination> {
        return repository.getSalaryAdvanceHistory(params.page)
    }

    data class Params(
        val page: Int
    )
}


