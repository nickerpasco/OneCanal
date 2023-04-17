package pe.com.onecanal.presentation.ui.features.mainDrawer.state

import pe.com.onecanal.domain.entity.BankAccount
import pe.com.onecanal.domain.entity.Failure
import pe.com.onecanal.domain.entity.UserProfileData

sealed class MainDrawerIntentState {
    object Loading : MainDrawerIntentState()
    data class Error(val error: Failure) : MainDrawerIntentState()
    data class GetUserSessionFromLocal(val userProfileData: UserProfileData?) : MainDrawerIntentState()
    object ClearUserSession : MainDrawerIntentState()
    object Idle : MainDrawerIntentState()

}
