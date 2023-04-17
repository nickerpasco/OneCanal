package pe.com.onecanal.presentation.ui.features.splash.state

import pe.com.onecanal.domain.entity.Failure
import pe.com.onecanal.domain.entity.UserProfileData

sealed class SplashIntentState {
    object Loading : SplashIntentState()
    data class Error(val error: Failure) : SplashIntentState()
    data class UserSessionFromLocalFetched(val userProfileData: UserProfileData?) : SplashIntentState()
    object UserSessionSaved:SplashIntentState()
    data class UserDataFromRemoteFetched(val userProfileData: UserProfileData) : SplashIntentState()
    object Idle : SplashIntentState()
}
