package pe.com.onecanal.presentation.ui.features.edit.state

import pe.com.onecanal.domain.entity.Failure
import pe.com.onecanal.domain.entity.MaritalStatus
import pe.com.onecanal.domain.entity.UserProfileData

sealed class EditIntentState {
    object Loading : EditIntentState()
    data class Error(val error: Failure) : EditIntentState()
    data class SaveProfile(val userProfileData: UserProfileData): EditIntentState()
    object UserSessionSaved: EditIntentState()
    data class GetMaritalStatus(val data: List<MaritalStatus>) : EditIntentState()
    data class GetUserSessionFromLocal(val userProfileData: UserProfileData?) : EditIntentState()
    object Idle : EditIntentState()

}