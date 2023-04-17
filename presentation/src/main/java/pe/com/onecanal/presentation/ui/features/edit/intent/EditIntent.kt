package pe.com.onecanal.presentation.ui.features.edit.intent

import pe.com.onecanal.domain.entity.UserProfileData

sealed class EditIntent {
    object GetMaritalStatus : EditIntent()
    class SaveProfile(val maritalType: Int?)  : EditIntent()
    object GetUserSession : EditIntent()
    data class UpdateUserSession(val userProfileData: UserProfileData): EditIntent()
}
