package pe.com.onecanal.presentation.ui.features.login.intent

import pe.com.onecanal.domain.entity.UserProfileData

/**
 * Created by Sergio Carrillo Diestra on 10/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
sealed class LoginIntent {
    object GetDocumentTypes : LoginIntent()
    object DoLogin : LoginIntent()
    object ForgotPassword : LoginIntent()
    object AccountValidation : LoginIntent()
    object GetUserDataFromRemote : LoginIntent()
    //    data class DoLogin(val documentType: String, val documentNumber: String, val password: String) :
//        LoginIntent()
    data class SaveUserSession(val userProfileData: UserProfileData) : LoginIntent()
    data class SaveUserSessionAccount(val userProfileData: UserProfileData) : LoginIntent()

}
