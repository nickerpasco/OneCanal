package pe.com.onecanal.presentation.ui.features.mainDrawer.intent

import pe.com.onecanal.domain.entity.BankAccount
import pe.com.onecanal.domain.entity.UserProfileData
import pe.com.onecanal.presentation.ui.base.BaseIntentCallback
import pe.com.onecanal.presentation.ui.features.mainDrawer.state.MainDrawerIntentState

/**
 * Created by Sergio Carrillo Diestra on 19/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
object MainDrawerIntentConfig {

    private lateinit var callback: MainDrawerIntentCallback

    fun instance(mainDrawerIntentCallback: MainDrawerIntentCallback): MainDrawerIntentConfig {
        callback = mainDrawerIntentCallback
        return this
    }

    fun collect(intentState: MainDrawerIntentState) {
        when (intentState) {
            MainDrawerIntentState.Idle -> {}
            is MainDrawerIntentState.Error -> callback.onError(intentState.error)
            is MainDrawerIntentState.GetUserSessionFromLocal -> callback.onUserSessionDataFetched(
                intentState.userProfileData
            )
            MainDrawerIntentState.Loading -> callback.onLoading()
            MainDrawerIntentState.ClearUserSession -> callback.onUserSessionCleared()
        }
    }

    interface MainDrawerIntentCallback : BaseIntentCallback {
        fun onUserSessionDataFetched(userProfileData: UserProfileData?)
        fun onUserSessionCleared()
    }

}