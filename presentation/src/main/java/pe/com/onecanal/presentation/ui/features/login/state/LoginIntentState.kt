package pe.com.onecanal.presentation.ui.features.login.state

import pe.com.onecanal.domain.entity.DocumentType
import pe.com.onecanal.domain.entity.Failure
import pe.com.onecanal.domain.entity.UserProfileData

sealed class LoginIntentState {
    object Loading : LoginIntentState()
    data class Error(val error: Failure) : LoginIntentState()
    data class DoLogin(val userProfileData: UserProfileData) : LoginIntentState()
    data class FetchDocumentTypes(val data: List<DocumentType>) : LoginIntentState()
    object SaveUserSession : LoginIntentState()
    object SaveUserSessionAccount : LoginIntentState()
    object ForgotPasswordClicked : LoginIntentState()
    object AccountValidationClicked : LoginIntentState()
    object Idle : LoginIntentState()
    data class UserDataFromRemoteFetched(val userProfileData: UserProfileData) : LoginIntentState()

    override fun equals(other: Any?): Boolean {
        return false
    }
}
