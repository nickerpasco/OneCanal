package pe.com.onecanal.presentation.ui.features.salaryAdvance.stepTwo.viewModel

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
import pe.com.onecanal.domain.usecases.GetBankAccountsFromSessionUseCase
import pe.com.onecanal.domain.usecases.GetSalaryAdvanceDetailsUseCase
import pe.com.onecanal.presentation.ui.features.salaryAdvance.stepTwo.intent.SalaryAdvanceStepTwoIntent
import pe.com.onecanal.presentation.ui.features.salaryAdvance.stepTwo.state.SalaryAdvanceStepTwoIntentState
import pe.com.onecanal.presentation.ui.util.FormField
import javax.inject.Inject

@HiltViewModel
class SalaryAdvanceStepTwoViewModel @Inject constructor(
    private val getBankAccountsUseCase: GetBankAccountsFromSessionUseCase,
    private val getSalaryAdvanceDetailsUseCase: GetSalaryAdvanceDetailsUseCase
) : ViewModel() {

    val userIntent = Channel<SalaryAdvanceStepTwoIntent>(Channel.UNLIMITED)
    private val _intentState =
        MutableStateFlow<SalaryAdvanceStepTwoIntentState>(SalaryAdvanceStepTwoIntentState.Idle)
    val intentState: StateFlow<SalaryAdvanceStepTwoIntentState> get() = _intentState

    val accountField = FormField(
        emptyResourceId = R.string.shouldSelectBankCard
    )

    init {
        observeUserIntent()
    }

    private fun observeUserIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    SalaryAdvanceStepTwoIntent.GetBankAccounts -> getBankAccounts()
                    is SalaryAdvanceStepTwoIntent.GetSalaryAdvanceDetails -> getSalaryAdvanceDetails(
                        it.amount
                    )
                }
            }

        }
    }

    private fun getBankAccounts() {
        _intentState.value = SalaryAdvanceStepTwoIntentState.Loading
        getBankAccountsUseCase.invoke(viewModelScope) { response ->
            response.either(
                { _intentState.value = SalaryAdvanceStepTwoIntentState.Error(it) },
                {_intentState.value =SalaryAdvanceStepTwoIntentState.BankAccountsFetched(it) })
        }
    }

    private fun getSalaryAdvanceDetails(amount: Double) {
        _intentState.value = SalaryAdvanceStepTwoIntentState.Loading
        getSalaryAdvanceDetailsUseCase.invoke(
            viewModelScope,
            GetSalaryAdvanceDetailsUseCase.Params(amount)
        ) { response ->
            response.either(
                { _intentState.value = SalaryAdvanceStepTwoIntentState.Error(it) },
                {
                    _intentState.value =
                        SalaryAdvanceStepTwoIntentState.SalaryAdvanceDetailsFetched(it)
                })
        }
    }

    fun fieldsAreValid(): Boolean {
        if (!accountField.fieldIsValid()) {
            return false
        }
        return true
    }
}
