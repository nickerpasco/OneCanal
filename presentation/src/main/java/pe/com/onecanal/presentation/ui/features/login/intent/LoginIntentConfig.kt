package pe.com.onecanal.presentation.ui.features.login.intent

import pe.com.onecanal.domain.entity.DocumentType
import pe.com.onecanal.domain.entity.UserProfileData
import pe.com.onecanal.presentation.ui.base.BaseIntentCallback
import pe.com.onecanal.presentation.ui.features.login.state.LoginIntentState

/**
 * Created by Sergio Carrillo Diestra on 12/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
object LoginIntentConfig {
    private lateinit var callback: LoginIntentCallback

    fun instance(callback: LoginIntentCallback): LoginIntentConfig {
        LoginIntentConfig.callback = callback
        return this
    }

    fun handleStates(intentState: LoginIntentState) {
        when (intentState) {
            LoginIntentState.Idle -> {}
            is LoginIntentState.Error -> callback.onError(intentState.error)
            is LoginIntentState.Loading -> callback.onLoading()
            is LoginIntentState.FetchDocumentTypes -> callback.onDocumentTypesFetched(intentState.data)
            is LoginIntentState.DoLogin -> callback.onLoginSuccess(intentState.userProfileData)
            is LoginIntentState.SaveUserSession -> callback.onUserSessionSaved()
            is LoginIntentState.SaveUserSessionAccount -> callback.onUserSessionSavedAccount()
            is LoginIntentState.AccountValidationClicked -> callback.onAccountValidationClicked()
            is LoginIntentState.ForgotPasswordClicked -> callback.onForgotPasswordClicked()
            is LoginIntentState.UserDataFromRemoteFetched -> callback.onUserDataFromRemoteFetched(intentState.userProfileData)
        }
    }

    interface LoginIntentCallback : BaseIntentCallback {
        fun onLoginSuccess(userProfileData: UserProfileData)
        fun onDocumentTypesFetched(data: List<DocumentType>)
        fun onUserSessionSaved()
        fun onUserSessionSavedAccount()
        fun onAccountValidationClicked()
        fun onForgotPasswordClicked()
        fun onUserDataFromRemoteFetched(userProfileData: UserProfileData)
    }

}