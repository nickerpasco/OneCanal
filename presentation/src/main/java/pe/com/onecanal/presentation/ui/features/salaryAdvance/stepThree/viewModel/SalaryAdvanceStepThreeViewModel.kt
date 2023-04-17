package pe.com.onecanal.presentation.ui.features.salaryAdvance.stepThree.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import pe.com.onecanal.domain.usecases.GetSalaryAdvanceFormatUseCase
import pe.com.onecanal.domain.usecases.GetTermsAndConditionsUseCase
import pe.com.onecanal.domain.usecases.SaveSalaryAdvanceUseCase
import pe.com.onecanal.domain.usecases.SaveSalaryAdvanceUseCase.Params
import pe.com.onecanal.presentation.ui.features.salaryAdvance.stepThree.intent.SalaryAdvanceStepThreeIntent
import pe.com.onecanal.presentation.ui.features.salaryAdvance.stepThree.state.SalaryAdvanceStepThreeIntentState
import javax.inject.Inject

@HiltViewModel
class SalaryAdvanceStepThreeViewModel @Inject constructor(
    private val saveSalaryAdvanceUseCase: SaveSalaryAdvanceUseCase,
    private val getSalaryAdvanceFormatUseCase: GetSalaryAdvanceFormatUseCase
) : ViewModel() {

    val userIntent = Channel<SalaryAdvanceStepThreeIntent>(Channel.UNLIMITED)
    private val _intentState =
        MutableStateFlow<SalaryAdvanceStepThreeIntentState>(SalaryAdvanceStepThreeIntentState.Idle)
    val intentState: StateFlow<SalaryAdvanceStepThreeIntentState> get() = _intentState

    init {
        observeUserIntent()
    }


    private fun observeUserIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is SalaryAdvanceStepThreeIntent.SaveSalaryAdvance -> saveSalaryAdvance(it.params)
                    SalaryAdvanceStepThreeIntent.GetTermsAndConditions -> getTermsAndConditions()
                }
            }

        }
    }

    private fun getTermsAndConditions() {
        _intentState.value = SalaryAdvanceStepThreeIntentState.Loading
        getSalaryAdvanceFormatUseCase.invoke(
            viewModelScope
        ) { response ->
            response.either(
                {
                    _intentState.value = SalaryAdvanceStepThreeIntentState.Error(it)
                },
                {
                    _intentState.value =
                        SalaryAdvanceStepThreeIntentState.TermsAndConditionsFetched(it)
                })
        }
    }

    private fun saveSalaryAdvance(params: Params) {
        _intentState.value = SalaryAdvanceStepThreeIntentState.Loading
        saveSalaryAdvanceUseCase.invoke(viewModelScope, params) { response ->
            response.either(
                { _intentState.value = SalaryAdvanceStepThreeIntentState.Error(it) },
                {
                    _intentState.value =
                        SalaryAdvanceStepThreeIntentState.SaveSalaryAdvanceSuccessful
                })
        }
    }
}
