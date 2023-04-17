package pe.com.onecanal.presentation.ui.features.mainDrawer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import pe.com.onecanal.domain.entity.BankAccount
import pe.com.onecanal.domain.entity.SalaryAdvanceDetails
import pe.com.onecanal.domain.entity.SalaryAdvanceReason
import pe.com.onecanal.domain.usecases.ClearUserSessionUseCase
import pe.com.onecanal.domain.usecases.GetBankAccountsFromSessionUseCase
import pe.com.onecanal.domain.usecases.GetUserSessionUseCase
import pe.com.onecanal.presentation.ui.features.mainDrawer.intent.MainDrawerIntent
import pe.com.onecanal.presentation.ui.features.mainDrawer.state.MainDrawerIntentState
import javax.inject.Inject

/**
 * Created by Sergio Carrillo Diestra on 10/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
@HiltViewModel
class MainDrawerViewModel @Inject constructor(
    private val getUserSessionUseCase: GetUserSessionUseCase,
    private val clearUserSessionUseCase: ClearUserSessionUseCase,
    private val getBankAccountsUseCase: GetBankAccountsFromSessionUseCase
) : ViewModel() {

    val userIntent = Channel<MainDrawerIntent>(Channel.UNLIMITED)
    private val _intentState = MutableStateFlow<MainDrawerIntentState>(MainDrawerIntentState.Idle)
    val intentState: StateFlow<MainDrawerIntentState> get() = _intentState

    init {
        observeUserIntent()
    }

    private fun observeUserIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is MainDrawerIntent.GetUserSession -> getUserSession()
                    MainDrawerIntent.ClearUserSession -> clearUserSession()
                }
            }
        }
    }

    private fun getUserSession() {
        _intentState.value = MainDrawerIntentState.Loading
        getUserSessionUseCase.invoke(viewModelScope) { response ->
            response.either(
                { _intentState.value = MainDrawerIntentState.Error(it) },
                { _intentState.value = MainDrawerIntentState.GetUserSessionFromLocal(it) })
        }
    }

    private fun clearUserSession() {
        _intentState.value = MainDrawerIntentState.Loading
        clearUserSessionUseCase.invoke(viewModelScope) { response ->
            response.either(
                { _intentState.value = MainDrawerIntentState.Error(it) },
                { _intentState.value = MainDrawerIntentState.ClearUserSession })
        }
    }
    val stepViewNumber = MutableLiveData(1)
    val showStepperView = MutableLiveData(true)

    var amount: Double = 0.0
    var account: BankAccount?=null
    var salaryAdvanceDetails: SalaryAdvanceDetails? = null
    var reason: SalaryAdvanceReason? = null


    fun dataForRequestIsValid(): Boolean {
        return try {
            return account != null &&
                    reason != null &&
                    salaryAdvanceDetails != null &&
                    salaryAdvanceDetails!!.fees.isNotEmpty() &&
                    salaryAdvanceDetails!!.transfer_amount.isNotEmpty() &&
                    salaryAdvanceDetails!!.period_name.isNotEmpty() &&
                    amount != 0.0 &&
                    reason!!.id != 0 &&
                    account!!.id != 0
        } catch (e: Exception) {
            false
        }
    }
}