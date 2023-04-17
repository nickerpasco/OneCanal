package pe.com.onecanal.presentation.ui.features.forgotPassoword.state

import pe.com.onecanal.domain.entity.DocumentType
import pe.com.onecanal.domain.entity.Failure
import pe.com.onecanal.domain.entity.ValidateData

/**
 * Created by Sergio Carrillo Diestra on 13/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
sealed class ForgotPasswordIntentState {
    object Loading : ForgotPasswordIntentState()
    data class Error(val error: Failure) : ForgotPasswordIntentState()
    data class ForgotPassword(val data: ValidateData) : ForgotPasswordIntentState()
    data class LoadDocumentTypes(val data: List<DocumentType>) : ForgotPasswordIntentState()
    object Idle : ForgotPasswordIntentState()
    object CancelClicked : ForgotPasswordIntentState()
    override fun equals(other: Any?): Boolean {
        return false
    }
}