package pe.com.onecanal.presentation.ui.features.newPassword.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import pe.com.onecanal.domain.usecases.CreateNewPasswordUseCase
import pe.com.onecanal.presentation.ui.features.newPassword.intent.NewPasswordIntent
import pe.com.onecanal.presentation.ui.features.newPassword.state.NewPasswordIntentState
import javax.inject.Inject

/**
 * Created by Sergio Carrillo Diestra on 12/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
@HiltViewModel
class NewPasswordViewModel @Inject constructor(private val createNewPasswordUseCase: CreateNewPasswordUseCase) :
    ViewModel() {

    val userIntent = Channel<NewPasswordIntent>(Channel.UNLIMITED)

    private val _intentState = MutableStateFlow<NewPasswordIntentState>(NewPasswordIntentState.Idle)
    val intentState: StateFlow<NewPasswordIntentState> get() = _intentState

    init {
        observeUserIntent()
    }

    private fun observeUserIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is NewPasswordIntent.CreateNewPassowrd -> createNewPassword(
                        it.code,
                        it.email,
                        it.userId,
                        it.password,
                        it.password_confirmation
                    )
                }
            }
        }
    }

    private fun createNewPassword(
        code: String,
        email: String,
        userId: Int,
        password: String,
        passwordConfirmation: String
    ) {

        _intentState.value = NewPasswordIntentState.Loading
        createNewPasswordUseCase.invoke(
            viewModelScope,
            CreateNewPasswordUseCase.Params(
                code,
                email,
                userId,
                password,
                passwordConfirmation
            )
        ) { response ->
            response.either(
                { _intentState.value = NewPasswordIntentState.Error(it) },
                { _intentState.value = NewPasswordIntentState.OnPasswordCreated })
        }
    }
}