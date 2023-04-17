package pe.com.onecanal.presentation.ui.features.validateAccount.state

import pe.com.onecanal.domain.entity.DocumentType
import pe.com.onecanal.domain.entity.Failure
import pe.com.onecanal.domain.entity.ValidateData

/**
 * Created by Sergio Carrillo Diestra on 10/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
sealed class AccountValidationState {
    object Idle : AccountValidationState()
    object Loading : AccountValidationState()
    data class Error(val error: Failure) : AccountValidationState()
    data class GetDocumentTypes(val documentTypes: List<DocumentType>) : AccountValidationState()
    data class ValidateAccount(val data: ValidateData) : AccountValidationState()
    data class TermsAndConditionsFetched(val terms: String) :AccountValidationState()
    data class ContractFetched(val terms: String) :AccountValidationState()
}
