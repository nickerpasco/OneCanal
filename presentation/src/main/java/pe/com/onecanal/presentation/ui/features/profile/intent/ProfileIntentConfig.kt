package pe.com.onecanal.presentation.ui.features.profile.intent

import pe.com.onecanal.domain.entity.UserProfileData
import pe.com.onecanal.presentation.ui.base.BaseIntentCallback
import pe.com.onecanal.presentation.ui.features.profile.state.ProfileIntentState

/**
 * Created by Sergio Carrillo Diestra on 19/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
object ProfileIntentConfig {

    private lateinit var callback: IntentCallback

    fun instance(callback:IntentCallback): ProfileIntentConfig {
        this.callback = callback
        return this
    }

    fun collect(intentState: ProfileIntentState) {
        when (intentState) {
            ProfileIntentState.Idle -> {}
            is ProfileIntentState.Error -> callback.onError(intentState.error)
            ProfileIntentState.Loading -> callback.onLoading()
            is ProfileIntentState.UserSessionFromLocalFetched -> callback.onUserSessionFromLocalFetched(intentState.userProfileData)
            ProfileIntentState.UserSessionSaved -> callback.onUserSessionSaved()
            is ProfileIntentState.UserDataFromRemoteFetched -> callback.onUserDataFromRemoteFetched(intentState.userProfileData)
        }
    }

    interface IntentCallback : BaseIntentCallback {
        fun onUserSessionFromLocalFetched(userProfileData: UserProfileData?)
        fun onUserDataFromRemoteFetched(userProfileData: UserProfileData)
        fun onUserSessionSaved()
    }

}