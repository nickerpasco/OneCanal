package pe.com.onecanal.presentation.ui.features.validateCode.intent

import pe.com.onecanal.presentation.ui.base.BaseIntentCallback
import pe.com.onecanal.presentation.ui.features.validateCode.state.CodeValidationState

object CodeValidationIntentConfig {
    private lateinit var callback: CodeValidationIntentCallback

    fun instance(intentCallback: CodeValidationIntentCallback): CodeValidationIntentConfig {
        callback= intentCallback
        return this
    }

    fun collect(intentState: CodeValidationState)
    {
        when (intentState) {
            CodeValidationState.Loading -> callback.onLoading()
            is CodeValidationState.Error -> callback.onError(intentState.error)
            CodeValidationState.OnValidateCode -> callback.onValidateCode()
            CodeValidationState.Idle -> {}
        }

    }

    interface CodeValidationIntentCallback: BaseIntentCallback {
        fun onValidateCode()
    }

}