package pe.com.onecanal.domain.usecases

import pe.com.onecanal.domain.entity.DocumentType
import pe.com.onecanal.domain.entity.Either
import pe.com.onecanal.domain.entity.Failure
import pe.com.onecanal.domain.repository.AppRepository
import pe.com.onecanal.domain.usecases.base.BaseUseCaseNoParams

/**
 * Created by Sergio Carrillo Diestra on 10/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
class GetDocumentTypesUseCase(private val appRepository: AppRepository) :
    BaseUseCaseNoParams<List<DocumentType>>() {
    override suspend fun run(): Either<Failure, List<DocumentType>> {
        return appRepository.getDocumentTypes()
    }
}