package pe.com.onecanal.presentation.ui.features.forgotPassoword.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pe.com.onecanal.domain.usecases.ForgotPasswordUseCase
import pe.com.onecanal.domain.usecases.GetDocumentTypesUseCase
import pe.com.onecanal.presentation.ui.features.forgotPassoword.intent.ForgetPasswordIntent
import pe.com.onecanal.presentation.ui.features.forgotPassoword.state.ForgotPasswordIntentState
import pe.com.onecanal.presentation.ui.model.ForgotPasswordFormFields
import javax.inject.Inject

/**
 * Created by Sergio Carrillo Diestra on 13/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val getDocumentTypesUseCase: GetDocumentTypesUseCase,
    private val forgotPasswordUseCase: ForgotPasswordUseCase
) : ViewModel() {

    val formFields = ForgotPasswordFormFields()

    private val _intentState =
        MutableStateFlow<ForgotPasswordIntentState>(ForgotPasswordIntentState.Idle)
    val intentState: StateFlow<ForgotPasswordIntentState> get() = _intentState

    private fun doForgetPassword() {
        if (formFields.fieldsAreValid()) {
            _intentState.value = ForgotPasswordIntentState.Loading
            forgotPasswordUseCase.invoke(
                viewModelScope,
                ForgotPasswordUseCase.Params(
                    formFields.documentType.fieldText.value!!,
                    formFields.documentNumber.fieldText.value!!
                )
            ) { response ->
                response.either(
                    { _intentState.value = ForgotPasswordIntentState.Error(it) },
                    { _intentState.value = ForgotPasswordIntentState.ForgotPassword(it) })
            }
        }
    }

    private fun getDocumentTypes() {
        viewModelScope.launch {
            _intentState.value = ForgotPasswordIntentState.Loading
            getDocumentTypesUseCase.run().either(
                { _intentState.value = ForgotPasswordIntentState.Error(it) },
                { _intentState.value = ForgotPasswordIntentState.LoadDocumentTypes(it) })
        }
    }


    fun sendIntent(intent: ForgetPasswordIntent) {
        when (intent) {
            is ForgetPasswordIntent.GetDocumentTypes -> getDocumentTypes()
            is ForgetPasswordIntent.ForgetPasswordEvent -> doForgetPassword()
            is ForgetPasswordIntent.DoCancel -> _intentState.value =
                ForgotPasswordIntentState.CancelClicked
        }
    }

    val SendEvent = ForgetPasswordIntent.ForgetPasswordEvent
    val cancelEvent = ForgetPasswordIntent.DoCancel
}