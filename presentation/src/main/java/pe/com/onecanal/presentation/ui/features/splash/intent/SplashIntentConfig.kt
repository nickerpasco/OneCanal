package pe.com.onecanal.presentation.ui.features.splash.intent

import pe.com.onecanal.domain.entity.UserProfileData
import pe.com.onecanal.presentation.ui.base.BaseIntentCallback
import pe.com.onecanal.presentation.ui.features.splash.state.SplashIntentState

/**
 * Created by Sergio Carrillo Diestra on 12/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
object SplashIntentConfig {
    private lateinit var callback: SplashIntentCallback

    fun instance(callback: SplashIntentCallback): SplashIntentConfig {
        SplashIntentConfig.callback = callback
        return this
    }

    fun collectIntentState(intentState: SplashIntentState) {
        when (intentState) {
            SplashIntentState.Idle -> {}
            is SplashIntentState.Error -> callback.onError(intentState.error)
            SplashIntentState.Loading -> callback.onLoading()
            is SplashIntentState.UserSessionFromLocalFetched -> callback.onUserSessionFromLocalFetched(intentState.userProfileData)
            SplashIntentState.UserSessionSaved -> callback.onUserSessionSaved()
            is SplashIntentState.UserDataFromRemoteFetched -> callback.onUserDataFromRemoteFetched(intentState.userProfileData)
        }
    }

    interface SplashIntentCallback : BaseIntentCallback {
        fun onUserSessionFromLocalFetched(userProfileData: UserProfileData?)
        fun onUserDataFromRemoteFetched(userProfileData: UserProfileData)
        fun onUserSessionSaved()
    }

}