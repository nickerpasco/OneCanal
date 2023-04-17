package pe.com.onecanal.presentation.ui.features.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import pe.com.onecanal.domain.entity.UserProfileData
import pe.com.onecanal.domain.usecases.*
import pe.com.onecanal.presentation.ui.features.login.intent.LoginIntent
import pe.com.onecanal.presentation.ui.features.login.state.LoginIntentState
import pe.com.onecanal.presentation.ui.model.LoginFormFields
import javax.inject.Inject

/**
 * Created by Sergio Carrillo Diestra on 13/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val getDocumentTypesUseCase: GetDocumentTypesUseCase,
    private val doLoginUseCase: DoLoginUseCase,
    private val saveUserSessionUseCase: SaveUserSessionUseCase,
    private val getUserDataFromRemoteUseCase: GetUserDataFromRemoteUseCase
) :
    ViewModel() {

    private val _intentState = MutableStateFlow<LoginIntentState>(LoginIntentState.Idle)
    val intentState: StateFlow<LoginIntentState> get() = _intentState

    val loginFormFields = LoginFormFields()

    private fun saveUserSession(userProfileData: UserProfileData) {
        _intentState.value = LoginIntentState.Loading
        saveUserSessionUseCase.invoke(viewModelScope, userProfileData) { response ->
            response.either(
                { _intentState.value = LoginIntentState.Error(it) },
                { _intentState.value = LoginIntentState.SaveUserSession })
        }
    }

    private fun saveUserSessionAccount(userProfileData: UserProfileData) {
        _intentState.value = LoginIntentState.Loading
        saveUserSessionUseCase.invoke(viewModelScope, userProfileData) { response ->
            response.either(
                { _intentState.value = LoginIntentState.Error(it) },
                { _intentState.value = LoginIntentState.SaveUserSessionAccount })
        }
    }

    private fun doLogin() {
        if (loginFormFields.fieldsAreValid()) {
            _intentState.value = LoginIntentState.Loading
            doLoginUseCase.invoke(
                viewModelScope, DoLoginUseCase.Params(
                    loginFormFields.documentType.fieldText.value!!,
                    loginFormFields.documentNumber.fieldText.value!!,
                    loginFormFields.password.fieldText.value!!
                )
            ) { response ->
                response.either(
                    { _intentState.value = LoginIntentState.Error(it) },
                    { _intentState.value = LoginIntentState.DoLogin(it) })
            }
        }

    }

    private fun getDocumentTypes() {
        _intentState.value = LoginIntentState.Loading
        getDocumentTypesUseCase.invoke(viewModelScope) { response ->
            response.either({
                _intentState.value = LoginIntentState.Error(it)
            }, {
                _intentState.value = LoginIntentState.FetchDocumentTypes(it)
            })
        }
    }

    fun sendIntent(action: LoginIntent) {
        when (action) {
            LoginIntent.GetDocumentTypes -> getDocumentTypes()
            LoginIntent.DoLogin -> doLogin()
            is LoginIntent.SaveUserSession -> saveUserSession(action.userProfileData)
            is LoginIntent.SaveUserSessionAccount -> saveUserSessionAccount(action.userProfileData)
            is LoginIntent.AccountValidation -> _intentState.value =
                LoginIntentState.AccountValidationClicked
            is LoginIntent.ForgotPassword -> _intentState.value =
                LoginIntentState.ForgotPasswordClicked
            LoginIntent.GetUserDataFromRemote -> getUserDataFromRemote()

        }
    }

    private fun getUserDataFromRemote() {
        _intentState.value = LoginIntentState.Loading
        getUserDataFromRemoteUseCase.invoke(viewModelScope) { response ->
            response.either(
                { _intentState.value = LoginIntentState.Error(it) },
                { _intentState.value = LoginIntentState.UserDataFromRemoteFetched(it) })
        }
    }

    companion object {
        val loginEvent = LoginIntent.DoLogin
        val forgotPasswordEvent = LoginIntent.ForgotPassword
        val accountValidationEvent = LoginIntent.AccountValidation
    }

}