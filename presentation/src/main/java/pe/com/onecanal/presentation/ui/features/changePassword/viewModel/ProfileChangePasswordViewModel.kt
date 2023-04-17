package pe.com.onecanal.presentation.ui.features.changePassword.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import pe.com.onecanal.R
import pe.com.onecanal.domain.usecases.ChangePasswordUseCase
import pe.com.onecanal.domain.usecases.ChangePasswordUseCase.Params
import pe.com.onecanal.presentation.ui.features.changePassword.intent.ChangePasswordIntent
import pe.com.onecanal.presentation.ui.features.changePassword.state.ChangePasswordIntentState
import pe.com.onecanal.presentation.ui.util.FormField
import javax.inject.Inject

@HiltViewModel
class ProfileChangePasswordViewModel @Inject constructor(
    private val changePasswordUseCase: ChangePasswordUseCase
) : ViewModel() {
    val userIntent = Channel<ChangePasswordIntent>(Channel.UNLIMITED)
    private val _intentState =
        MutableStateFlow<ChangePasswordIntentState>(ChangePasswordIntentState.Idle)
    val intentState: StateFlow<ChangePasswordIntentState> get() = _intentState

    val currentPasswordField = FormField(emptyResourceId = R.string.digitCurrentPassword)
    val newPasswordField = FormField(emptyResourceId = R.string.digitNewPassword)
    val repeatNewPasswordField = FormField(emptyResourceId = R.string.repetNewPassword)

    init {
        observeUserIntent()
    }

    private fun observeUserIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is ChangePasswordIntent.ChangePassword -> changePassword(it.params)
                }
            }
        }
    }

    private fun changePassword(
        params: Params
    ) {
        _intentState.value = ChangePasswordIntentState.Loading
        changePasswordUseCase.invoke(
            viewModelScope,
            params
        ) { response ->
            response.either(
                { _intentState.value = ChangePasswordIntentState.Error(it) },
                { _intentState.value = ChangePasswordIntentState.PasswordChanged })
        }
    }

    private fun fieldsAreValid(): Boolean {
        if (!currentPasswordField.fieldIsValid() || !newPasswordField.fieldIsValid() || !repeatNewPasswordField.fieldIsValid())
            return false
        return true
    }

    fun changePassword() {
        if (fieldsAreValid()) {
            changePassword(
                Params(
                    currentPasswordField.fieldText.value!!,
                    newPasswordField.fieldText.value!!,
                    repeatNewPasswordField.fieldText.value!!
                )
            )
        }
    }

}
