package pe.com.onecanal.presentation.ui.features.validateAccount.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import pe.com.onecanal.domain.usecases.AccountValidationUseCase
import pe.com.onecanal.domain.usecases.GetDocumentTypesUseCase
import pe.com.onecanal.domain.usecases.GetTermsAndConditionsUseCase
import pe.com.onecanal.presentation.ui.features.validateAccount.intent.AccountValidationIntent
import pe.com.onecanal.presentation.ui.features.validateAccount.state.AccountValidationState
import pe.com.onecanal.presentation.ui.model.AccountValidationFormFields
import javax.inject.Inject

/**
 * Created by Sergio Carrillo Diestra on 10/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/

@HiltViewModel
class AccountValidationViewModel @Inject constructor(
    private val getDocumentTypesUseCase: GetDocumentTypesUseCase,
    private val accountValidationUseCase: AccountValidationUseCase,
    private val getTermsAndConditionsUseCase: GetTermsAndConditionsUseCase
) :
    ViewModel() {

    val userIntent = Channel<AccountValidationIntent>(Channel.UNLIMITED)

    private val _intentState = MutableStateFlow<AccountValidationState>(AccountValidationState.Idle)
    val intentState: StateFlow<AccountValidationState> get() = _intentState

    val accountValidationFormFields = AccountValidationFormFields()
    init {
        observeUserIntent()
    }

    private fun observeUserIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is AccountValidationIntent.AccountValidation -> validateAccount(
                        it.documentType,
                        it.documentNumber
                    )
                    AccountValidationIntent.GetDocumentTypes -> getDocumentTypes()
                    AccountValidationIntent.GetTermsAndConditions -> getTermsAndConditions()
                    AccountValidationIntent.GetContract -> getContract()
                }
            }
        }
    }

    private fun getContract() {
        _intentState.value = AccountValidationState.Loading
        getTermsAndConditionsUseCase.invoke(
            viewModelScope,
            GetTermsAndConditionsUseCase.Params(type = "contract")
        ) { response ->
            response.either(
                {
                    _intentState.value = AccountValidationState.Error(it)
                },
                {
                    _intentState.value =
                        AccountValidationState.ContractFetched(it)
                })
        }
    }

    private fun getTermsAndConditions() {
        _intentState.value = AccountValidationState.Loading
        getTermsAndConditionsUseCase.invoke(
            viewModelScope,
            GetTermsAndConditionsUseCase.Params(type = null)
        ) { response ->
            response.either(
                {
                    _intentState.value = AccountValidationState.Error(it)
                },
                {
                    _intentState.value =
                        AccountValidationState.TermsAndConditionsFetched(it)
                })
        }
    }

    private fun validateAccount(documentType: String, documentNumber: String) {
        if (accountValidationFormFields.fieldsAreValid()) {
            _intentState.value = AccountValidationState.Loading
            accountValidationUseCase.invoke(
                viewModelScope,
                AccountValidationUseCase.Params(
                    documentType,
                    documentNumber
                )
            ) { response ->
                response.either(
                    { _intentState.value = AccountValidationState.Error(it) },
                    { _intentState.value = AccountValidationState.ValidateAccount(it) })
            }
        }
    }

    private fun getDocumentTypes() {
        viewModelScope.launch {
            _intentState.value = AccountValidationState.Loading
            getDocumentTypesUseCase.invoke(viewModelScope) { response ->
                response.either(
                    { _intentState.value = AccountValidationState.Error(it) },
                    { _intentState.value = AccountValidationState.GetDocumentTypes(it) })
            }
        }
    }

}