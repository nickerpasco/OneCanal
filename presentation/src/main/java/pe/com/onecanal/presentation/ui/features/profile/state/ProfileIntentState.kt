package pe.com.onecanal.presentation.ui.features.profile.state

import pe.com.onecanal.domain.entity.Failure
import pe.com.onecanal.domain.entity.UserProfileData

sealed class ProfileIntentState {
    object Loading : ProfileIntentState()
    data class Error(val error: Failure) : ProfileIntentState()
    data class UserSessionFromLocalFetched(val userProfileData: UserProfileData?) : ProfileIntentState()
    data class UserDataFromRemoteFetched(val userProfileData: UserProfileData) : ProfileIntentState()
    object UserSessionSaved: ProfileIntentState()
    object Idle : ProfileIntentState()
}
