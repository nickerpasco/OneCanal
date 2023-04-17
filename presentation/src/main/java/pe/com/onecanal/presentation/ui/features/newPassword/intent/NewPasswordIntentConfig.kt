package pe.com.onecanal.presentation.ui.features.newPassword.intent

import pe.com.onecanal.presentation.ui.base.BaseIntentCallback
import pe.com.onecanal.presentation.ui.features.newPassword.state.NewPasswordIntentState

/**
 * Created by Sergio Carrillo Diestra on 12/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
object NewPasswordIntentConfig {
    private lateinit var callback: NewPasswordIntentCallback

    fun instance(callback: NewPasswordIntentCallback):NewPasswordIntentConfig{
        NewPasswordIntentConfig.callback=callback
        return this
    }

    fun collect(intentState: NewPasswordIntentState)
    {
        when (intentState) {
            NewPasswordIntentState.Loading -> callback.onLoading()
            is NewPasswordIntentState.Error -> callback.onError(intentState.error)
            NewPasswordIntentState.OnPasswordCreated -> callback.onNewPasswordCreated()
            NewPasswordIntentState.Idle -> {}
        }

    }

    interface NewPasswordIntentCallback: BaseIntentCallback {
        fun onNewPasswordCreated()
    }

}