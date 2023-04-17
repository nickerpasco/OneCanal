package pe.com.onecanal.presentation.ui.features.edit.intent

import pe.com.onecanal.domain.entity.MaritalStatus
import pe.com.onecanal.domain.entity.UserProfileData
import pe.com.onecanal.presentation.ui.base.BaseIntentCallback
import pe.com.onecanal.presentation.ui.features.bank.intent.BankIntentConfig
import pe.com.onecanal.presentation.ui.features.bank.state.BankIntentState
import pe.com.onecanal.presentation.ui.features.edit.state.EditIntentState

object EditIntentConfig {

    private lateinit var callback: IntentCallback

    fun instance(intentCallback: IntentCallback): EditIntentConfig {
        callback = intentCallback
        return this
    }

    fun collect(intentState: EditIntentState) {
        when (intentState) {
            EditIntentState.Idle -> {}
            is EditIntentState.Error -> callback.onError(intentState.error)
            is EditIntentState.SaveProfile -> callback.onDoEditSuccess(intentState.userProfileData)
            is EditIntentState.GetMaritalStatus -> callback.onGetMaritalStatus(intentState.data)
            EditIntentState.UserSessionSaved -> callback.onUserSessionSaved()
            is EditIntentState.GetUserSessionFromLocal -> callback.onUserSessionDataFetched(intentState.userProfileData)
            EditIntentState.UserSessionSaved -> callback.onUserSessionSaved()
            EditIntentState.Loading -> callback.onLoading()
        }
    }

    interface IntentCallback : BaseIntentCallback {
        fun onDoEditSuccess(userProfileData: UserProfileData?)
        fun onUserSessionDataFetched(userProfileData: UserProfileData?)
        fun onGetMaritalStatus(data: List<MaritalStatus>)
        fun onUserSessionSaved()
    }

}