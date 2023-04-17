package pe.com.onecanal.domain.usecases

import pe.com.onecanal.domain.entity.Either
import pe.com.onecanal.domain.entity.Failure
import pe.com.onecanal.domain.entity.FeeDomain
import pe.com.onecanal.domain.entity.FeeType
import pe.com.onecanal.domain.repository.AppRepository
import pe.com.onecanal.domain.usecases.base.BaseUseCaseNoParams

/**
 * Created by Sergio Carrillo Diestra on 18/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
class GetFeesUseCase(private val repository: AppRepository) : BaseUseCaseNoParams<FeeDomain>() {
    override suspend fun run(): Either<Failure, FeeDomain> = repository.getFees()
}