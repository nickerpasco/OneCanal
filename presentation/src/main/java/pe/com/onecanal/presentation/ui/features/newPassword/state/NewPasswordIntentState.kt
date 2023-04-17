package pe.com.onecanal.presentation.ui.features.newPassword.state

import pe.com.onecanal.domain.entity.Failure

sealed class NewPasswordIntentState {
    object Loading : NewPasswordIntentState()
    data class Error(val error: Failure) : NewPasswordIntentState()
    object OnPasswordCreated : NewPasswordIntentState()
    object Idle : NewPasswordIntentState()
}
