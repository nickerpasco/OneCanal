package pe.com.onecanal.presentation.ui.features.history.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import pe.com.onecanal.domain.usecases.GetHistoryUseCase
import pe.com.onecanal.domain.usecases.GetHistoryUseCase.*
import pe.com.onecanal.domain.usecases.GetUserSessionUseCase
import pe.com.onecanal.presentation.ui.features.history.intent.HistoryIntent
import pe.com.onecanal.presentation.ui.features.history.state.HistoryIntentState
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getUserSessionUseCase: GetUserSessionUseCase,
    private val getHistoryUseCase: GetHistoryUseCase
) :
    ViewModel() {

    val userIntent = Channel<HistoryIntent>(Channel.UNLIMITED)
    private val _intentState = MutableStateFlow<HistoryIntentState>(HistoryIntentState.Idle)
    val intentState: StateFlow<HistoryIntentState> get() = _intentState

    init {
        observeUserIntent()
    }

    private fun observeUserIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is HistoryIntent.GetUserSession -> getUserSession()
                    is HistoryIntent.GetHistory -> getHistory(it.page)
                }
            }
        }
    }

    private fun getUserSession() {
//        _intentState.value = HistoryIntentState.Loading
        getUserSessionUseCase.invoke(viewModelScope) { response ->
            response.either(
                { _intentState.value = HistoryIntentState.Error(it) },
                { _intentState.value = HistoryIntentState.GetUserSession(it) })
        }
    }

    private fun getHistory(page: Int) {
        _intentState.value = HistoryIntentState.Loading
        getHistoryUseCase.invoke(viewModelScope, Params(page)) { response ->
            response.either(
                { _intentState.value = HistoryIntentState.Error(it) },
                { _intentState.value = HistoryIntentState.GetHistory(it) })
        }
    }

}
