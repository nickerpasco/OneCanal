package pe.com.onecanal.presentation.ui.features.salaryAdvance.stepOne.viewModel

import android.text.InputType
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
import pe.com.onecanal.domain.entity.Failure
import pe.com.onecanal.domain.entity.FeeItem
import pe.com.onecanal.domain.entity.UserProfileData
import pe.com.onecanal.domain.usecases.*
import pe.com.onecanal.domain.usecases.SaveSalarySwitchStateUseCase.Params
import pe.com.onecanal.presentation.ui.extensions.isANumber
import pe.com.onecanal.presentation.ui.extensions.toDoubleWithTwoDecimals
import pe.com.onecanal.presentation.ui.features.salaryAdvance.stepOne.intent.SalaryAdvanceStepOneIntent
import pe.com.onecanal.presentation.ui.features.salaryAdvance.stepOne.state.SalaryAdvanceStepOneIntentState
import pe.com.onecanal.presentation.ui.util.FormField
import javax.inject.Inject

/**
 * Created by Sergio Carrillo Diestra on 18/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
@HiltViewModel
class SalaryAdvanceStepOneViewModel @Inject constructor(
    private val getFeesUseCase: GetFeesUseCase,
    private val getSalaryAdvanceReasonsUseCase: GetSalaryAdvanceReasonsUseCase,
    private val getSalaryUseCase: GetSalaryUseCase,
    private val getSalarySwitchStateUseCase: GetSalarySwitchStateUseCase,
    private val saveSalarySwitchStateUseCase: SaveSalarySwitchStateUseCase,
    private val getUserSessionUseCase: GetUserSessionUseCase,
    private val getUserDataFromRemoteUseCase: GetUserDataFromRemoteUseCase,
    private val saveUserSessionUseCase: SaveUserSessionUseCase
) : ViewModel() {

    val userIntent = Channel<SalaryAdvanceStepOneIntent>(Channel.UNLIMITED)
    val _intentState =
        MutableStateFlow<SalaryAdvanceStepOneIntentState>(SalaryAdvanceStepOneIntentState.Idle)
    val intentState: StateFlow<SalaryAdvanceStepOneIntentState> get() = _intentState

    //FORM FIELDS REGION
    val availableSalaryAmountField = FormField(
        inputTypeId = InputType.TYPE_NUMBER_FLAG_DECIMAL,
        invalidFieldResourceId = R.string.theEnteredValueIsIncorrect,
        emptyResourceId = R.string.DigitSalaryAdvanceAmount,
        toBigValueErrorResourceId = R.string.inputExceedsTheMaximunAvailable,
        hintResourceId = R.string.digitSalaryAdvance
    )

    val advanceReasonField = FormField(
        emptyResourceId = R.string.shouldSelectSalaryAdvanceReason,
        hintResourceId = R.string.shouldSelectBankCard
    )

    init {
        observeUserIntent()
    }

    private fun observeUserIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    SalaryAdvanceStepOneIntent.GetFees -> getFees()
                    SalaryAdvanceStepOneIntent.GetSalaryAdvanceReasons -> getSalaryAdvanceReasons()
                    SalaryAdvanceStepOneIntent.GetSalary -> getSalary()
                    SalaryAdvanceStepOneIntent.GetSalarySwitchState -> getSalarySwitchState()
                    is SalaryAdvanceStepOneIntent.SaveSalarySwitchState -> saveSalarySwitchState(it.state)
                    SalaryAdvanceStepOneIntent.GetUserSessionFromLocal -> getUserSessionFromLocal()
                    SalaryAdvanceStepOneIntent.GetUserDataFromRemote -> getUserDataFromRemote()
                    is SalaryAdvanceStepOneIntent.UpdateUserSession -> saveUserSession(it.userProfileData)
                }
            }
        }
    }

    private fun saveSalarySwitchState(state: Boolean) {
        _intentState.value = SalaryAdvanceStepOneIntentState.Loading
        saveSalarySwitchStateUseCase.invoke(
            viewModelScope,
            Params(state)
        ) { response ->
            response.either(
                { _intentState.value = SalaryAdvanceStepOneIntentState.Error(it) },
                { _intentState.value = SalaryAdvanceStepOneIntentState.SalarySwitchStateSaved })
        }
    }

    private fun getSalarySwitchState() {
        try {
            _intentState.value =
                SalaryAdvanceStepOneIntentState.SalarySwitchStateFetched(getSalarySwitchStateUseCase.invoke())
        } catch (e: Exception) {
            _intentState.value =
                SalaryAdvanceStepOneIntentState.Error(Failure.ReadFromLocalDatasourceError)
        }
    }

    private fun getSalary() {
        _intentState.value = SalaryAdvanceStepOneIntentState.Loading
        getSalaryUseCase.invoke(viewModelScope) { response ->
            response.either(
                { _intentState.value = SalaryAdvanceStepOneIntentState.Error(it) },
                { _intentState.value = SalaryAdvanceStepOneIntentState.SalaryFetched(it) })
        }
    }

    private fun getSalaryAdvanceReasons() {
        _intentState.value = SalaryAdvanceStepOneIntentState.Loading
        getSalaryAdvanceReasonsUseCase.invoke(viewModelScope) { response ->
            response.either(
                { _intentState.value = SalaryAdvanceStepOneIntentState.Error(it) },
                {
                    _intentState.value =
                        SalaryAdvanceStepOneIntentState.SalaryAdvanceReasonFetched(it)
                })
        }
    }

    private fun getFees() {
        _intentState.value = SalaryAdvanceStepOneIntentState.Loading
        getFeesUseCase.invoke(viewModelScope) { response ->
            response.either(
                { _intentState.value = SalaryAdvanceStepOneIntentState.Error(it) },
                {
                    _intentState.value =
                        SalaryAdvanceStepOneIntentState.FeesFetched(it)
                })
        }
    }

    fun fieldsAreValid(): Boolean {
        if (!availableSalaryAmountField.fieldIsValid() || !advanceReasonField.fieldIsValid()) {
            return false
        }
        return true
    }

    private fun saveUserSession(userProfileData: UserProfileData) {
        saveUserSessionUseCase.invoke(viewModelScope, userProfileData) { response ->
            response.either(
                { _intentState.value = SalaryAdvanceStepOneIntentState.Error(it) },
                { _intentState.value = SalaryAdvanceStepOneIntentState.UserSessionSaved })
        }
    }

    private fun getUserSessionFromLocal() {
        _intentState.value = SalaryAdvanceStepOneIntentState.Loading
        getUserSessionUseCase.invoke(viewModelScope) { response ->
            response.either(
                { _intentState.value = SalaryAdvanceStepOneIntentState.Error(it) },
                { _intentState.value = SalaryAdvanceStepOneIntentState.UserSessionFromLocalFetched(it) })
        }
    }

    private fun getUserDataFromRemote() {
        _intentState.value = SalaryAdvanceStepOneIntentState.Loading
        getUserDataFromRemoteUseCase.invoke(viewModelScope) { response ->
            response.either(
                {
                    _intentState.value = SalaryAdvanceStepOneIntentState.Error(it)
                },
                {
                    _intentState.value = SalaryAdvanceStepOneIntentState.UserDataFromRemoteFetched(it)
                })
        }
    }


    fun onSalaryAdvanceTextChanged(feeRanges: Array<FeeItem>, text: String): Double {
        var feeValue:Double = 0.0
        if (text.isNotEmpty() && text.isANumber()) {
            text.toDoubleWithTwoDecimals().apply {
                for (feeItem in feeRanges) {
                    if (this in feeItem.min.toDoubleWithTwoDecimals()..feeItem.max.toDoubleWithTwoDecimals())
                        feeValue = feeItem.fee
                }
            }
        }
        else feeValue = 0.00
        return feeValue
    }

}