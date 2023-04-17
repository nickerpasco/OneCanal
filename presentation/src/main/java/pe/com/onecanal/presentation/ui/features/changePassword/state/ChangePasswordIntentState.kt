package pe.com.onecanal.presentation.ui.features.changePassword.state

import pe.com.onecanal.domain.entity.Failure

/**
 * Created by Sergio Carrillo Diestra on 18/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
sealed class ChangePasswordIntentState {
    object Loading : ChangePasswordIntentState()
    data class Error(val error: Failure) : ChangePasswordIntentState()
    object PasswordChanged : ChangePasswordIntentState()
    object Idle : ChangePasswordIntentState()
}