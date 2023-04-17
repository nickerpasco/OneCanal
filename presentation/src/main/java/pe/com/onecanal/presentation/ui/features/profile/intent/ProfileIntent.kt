package pe.com.onecanal.presentation.ui.features.profile.intent

import pe.com.onecanal.domain.entity.UserProfileData

sealed class ProfileIntent {
    object GetUserSessionFromLocal : ProfileIntent()
    object GetUserDataFromRemote : ProfileIntent()
    data class UpdateUserSession(val userProfileData: UserProfileData): ProfileIntent()
}
