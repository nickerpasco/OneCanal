package pe.com.onecanal.domain.usecases

import pe.com.onecanal.domain.repository.AppRepository

/**
 * Created by Sergio Carrillo Diestra on 18/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
class GetSalarySwitchStateUseCase(private val repository: AppRepository) {
    fun invoke(): Boolean {
        return repository.getSalarySwitchState()
    }
}