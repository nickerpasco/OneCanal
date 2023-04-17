package pe.com.onecanal.presentation.ui.features.changePassword.intent

import pe.com.onecanal.domain.usecases.ChangePasswordUseCase

/**
 * Created by Sergio Carrillo Diestra on 18/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
sealed class ChangePasswordIntent {
    data class ChangePassword(
        val params: ChangePasswordUseCase.Params
    ) : ChangePasswordIntent()
}