package pe.com.onecanal.domain.usecases

import pe.com.onecanal.domain.entity.Either
import pe.com.onecanal.domain.entity.Failure
import pe.com.onecanal.domain.entity.UserProfileData
import pe.com.onecanal.domain.repository.AppRepository
import pe.com.onecanal.domain.usecases.base.BaseUseCase

class SaveProfileUseCase(private val repository: AppRepository) :
    BaseUseCase<UserProfileData, SaveProfileUseCase.Params>() {
    data class Params(
        val maritalStatusId: Int,
        val address: String,
        val businessJob: String,
        val salary: String
    )

    override suspend fun run(params: Params): Either<Failure, UserProfileData> =
        repository.completeProfile(
            maritalStatusId = params.maritalStatusId,
            address = params.address,
            businessJob = params.businessJob,
            salary = params.salary
        )
}