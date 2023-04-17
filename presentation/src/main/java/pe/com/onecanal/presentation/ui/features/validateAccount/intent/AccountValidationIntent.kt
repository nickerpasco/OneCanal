package pe.com.onecanal.presentation.ui.features.validateAccount.intent

/**
 * Created by Sergio Carrillo Diestra on 10/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
sealed class AccountValidationIntent {
    object GetDocumentTypes : AccountValidationIntent()
    class AccountValidation(val documentType: String, val documentNumber: String) :
        AccountValidationIntent()

    object GetTermsAndConditions : AccountValidationIntent()
    object GetContract : AccountValidationIntent()
}
