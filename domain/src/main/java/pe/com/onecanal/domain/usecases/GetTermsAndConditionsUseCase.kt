package pe.com.onecanal.domain.usecases

import pe.com.onecanal.domain.entity.Either
import pe.com.onecanal.domain.entity.Failure
import pe.com.onecanal.domain.repository.AppRepository
import pe.com.onecanal.domain.usecases.base.BaseUseCase

/**
 * Created by Sergio Carrillo Diestra on 18/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
class GetTermsAndConditionsUseCase(private val repository: AppRepository) :
    BaseUseCase<String, GetTermsAndConditionsUseCase.Params>() {
    data class Params(
        val type: String? = null,
    )
    override suspend fun run(params: Params): Either<Failure, String> = repository.getTermsAndConditions(type = params.type)
}