package pe.com.onecanal.presentation.ui.features.forgotPassoword.intent

import pe.com.onecanal.domain.entity.DocumentType
import pe.com.onecanal.domain.entity.ValidateData
import pe.com.onecanal.presentation.ui.base.BaseIntentCallback
import pe.com.onecanal.presentation.ui.features.forgotPassoword.state.ForgotPasswordIntentState

/**
 * Created by Sergio Carrillo Diestra on 13/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
object ForgotPasswordIntentConfig {

    private lateinit var callback: ForgotPasswordCallback

    fun instance(callback: ForgotPasswordCallback): ForgotPasswordIntentConfig {
        ForgotPasswordIntentConfig.callback = callback
        return this
    }

    fun handleStates(intentState: ForgotPasswordIntentState) {
        when (intentState) {

            is ForgotPasswordIntentState.Error -> callback.onError(intentState.error)
            is ForgotPasswordIntentState.Loading -> callback.onLoading()
            is ForgotPasswordIntentState.LoadDocumentTypes -> callback.onDocumentTypesLoaded(
                intentState.data
            )
            is ForgotPasswordIntentState.ForgotPassword -> callback.onDoForgotEventSuccess(intentState.data)
            is ForgotPasswordIntentState.Idle -> {}
            is ForgotPasswordIntentState.CancelClicked -> callback.onCancelClicked()
        }
    }

    interface ForgotPasswordCallback : BaseIntentCallback {
        fun onDoForgotEventSuccess(data: ValidateData)
        fun onDocumentTypesLoaded(data: List<DocumentType>)
        fun onCancelClicked()
        fun onSendButtonClicked()
    }
}