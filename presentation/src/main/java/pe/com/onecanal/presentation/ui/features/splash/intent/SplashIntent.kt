package pe.com.onecanal.presentation.ui.features.splash.intent

import pe.com.onecanal.domain.entity.UserProfileData

/**
 * Created by Sergio Carrillo Diestra on 10/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
sealed class SplashIntent {
    object GetUserSessionFromLocal : SplashIntent()
    object GetUserDataFromRemote : SplashIntent()
    data class UpdateUserSession(val userProfileData: UserProfileData):SplashIntent()
}
