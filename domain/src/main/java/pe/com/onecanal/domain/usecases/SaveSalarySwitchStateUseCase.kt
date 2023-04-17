package pe.com.onecanal.domain.usecases

import pe.com.onecanal.domain.entity.Either
import pe.com.onecanal.domain.entity.Failure
import pe.com.onecanal.domain.repository.AppRepository
import pe.com.onecanal.domain.usecases.base.BaseUseCase

class SaveSalarySwitchStateUseCase(private val repository: AppRepository) :
    BaseUseCase<Unit, SaveSalarySwitchStateUseCase.Params>() {
    data class Params(
        val switchState: Boolean
    )

    override suspend fun run(params: Params): Either<Failure, Unit> =
        repository.saveSalarySwitchState(
            params.switchState
        )
}
