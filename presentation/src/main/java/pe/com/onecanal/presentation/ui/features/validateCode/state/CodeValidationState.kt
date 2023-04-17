package pe.com.onecanal.presentation.ui.features.validateCode.state

import pe.com.onecanal.domain.entity.Failure

sealed class CodeValidationState {
    object Loading : CodeValidationState()
    data class Error(val error: Failure) : CodeValidationState()
    object OnValidateCode : CodeValidationState()
    object Idle : CodeValidationState()
}
