package pe.com.onecanal.presentation.ui.features.changePassword.intent

import pe.com.onecanal.presentation.ui.base.BaseIntentCallback
import pe.com.onecanal.presentation.ui.features.changePassword.state.ChangePasswordIntentState

/**
 * Created by Sergio Carrillo Diestra on 18/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
object ChangePasswordIntentConfig {

    private lateinit var callback: IntentCallback

    fun instance(baseIntentCallback: IntentCallback): ChangePasswordIntentConfig {
        callback = baseIntentCallback
        return this
    }

    fun collectIntentState(intentState: ChangePasswordIntentState) {
        when (intentState) {
            is ChangePasswordIntentState.Error -> callback.onError(intentState.error)
            ChangePasswordIntentState.Loading -> callback.onLoading()
            ChangePasswordIntentState.Idle -> {}
            ChangePasswordIntentState.PasswordChanged -> callback.onPasswordChanged()
        }
    }

    interface IntentCallback : BaseIntentCallback {
        fun onPasswordChanged()
    }
}