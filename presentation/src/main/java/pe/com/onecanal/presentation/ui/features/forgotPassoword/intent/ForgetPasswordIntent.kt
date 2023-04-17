package pe.com.onecanal.presentation.ui.features.forgotPassoword.intent

/**
 * Created by Sergio Carrillo Diestra on 13/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
sealed class ForgetPasswordIntent {
    object GetDocumentTypes : ForgetPasswordIntent()
    object DoCancel : ForgetPasswordIntent()
    object ForgetPasswordEvent : ForgetPasswordIntent()
}