package pe.com.onecanal.presentation.ui.features.validateCode.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import pe.com.onecanal.domain.usecases.ValidCodeUseCase
import pe.com.onecanal.presentation.ui.features.validateCode.intent.CodeValidationIntent
import pe.com.onecanal.presentation.ui.features.validateCode.state.CodeValidationState
import javax.inject.Inject

@HiltViewModel
class CodeValidationViewModel @Inject constructor(private val validCodeUseCase: ValidCodeUseCase) : ViewModel() {


    val userIntent = Channel<CodeValidationIntent>(Channel.UNLIMITED)

    private val _intentState = MutableStateFlow<CodeValidationState>(CodeValidationState.Idle)
    val intentState: StateFlow<CodeValidationState> get() = _intentState

    init {
        observeUserIntent()
    }

    private fun observeUserIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is CodeValidationIntent.ValidateCode -> validateCode(
                        it.code,
                        it.email,
                        it.userId
                    )
                }
            }
        }
    }

    private fun validateCode(
        code: String,
        email: String,
        userId: Int
    ) {
        _intentState.value = CodeValidationState.Loading
        validCodeUseCase.invoke(
            viewModelScope,
            ValidCodeUseCase.Params(
                code,
                email,
                userId
            )
        ) { response ->
            response.either(
                { _intentState.value = CodeValidationState.Error(it) },
                { _intentState.value = CodeValidationState.OnValidateCode })
        }
    }
}