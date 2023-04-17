package pe.com.onecanal.presentation.ui.features.validateAccount.intent

import pe.com.onecanal.domain.entity.DocumentType
import pe.com.onecanal.domain.entity.UserProfileData
import pe.com.onecanal.domain.entity.ValidateData
import pe.com.onecanal.presentation.ui.base.BaseIntentCallback
import pe.com.onecanal.presentation.ui.features.validateAccount.state.AccountValidationState

/**
 * Created by Sergio Carrillo Diestra on 10/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
object AccountValidationIntentConfig {

    private lateinit var intentCallback: IntentCallback

    fun instance(intentCallback: IntentCallback): AccountValidationIntentConfig {
        AccountValidationIntentConfig.intentCallback = intentCallback
        return this
    }

    fun mainCollect(intentState: AccountValidationState) {

        when (intentState) {
            is AccountValidationState.Loading -> {
                intentCallback.onLoading()
            }
            is AccountValidationState.GetDocumentTypes -> {
                intentCallback.onDocumentTypesReady(intentState.documentTypes)
            }
            is AccountValidationState.Error -> {
                intentCallback.onError(intentState.error)
            }
            is AccountValidationState.ValidateAccount -> intentCallback.onAccountValidationSuccess(intentState.data)
            AccountValidationState.Idle -> {}
            is AccountValidationState.TermsAndConditionsFetched -> intentCallback.onTermsAndConditionsFetched(
                intentState.terms
            )
            is AccountValidationState.ContractFetched -> intentCallback.onContractFetched(
                intentState.terms
            )
        }
    }

    interface IntentCallback : BaseIntentCallback {
        fun onDocumentTypesReady(data: List<DocumentType>)
        fun onAccountValidationSuccess(data: ValidateData)
        fun onTermsAndConditionsFetched(terms: String)
        fun onContractFetched(doc: String)
    }
}

