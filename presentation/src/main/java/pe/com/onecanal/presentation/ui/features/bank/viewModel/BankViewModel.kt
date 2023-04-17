package pe.com.onecanal.presentation.ui.features.bank.viewModel

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
import pe.com.onecanal.domain.entity.UserProfileData
import pe.com.onecanal.domain.usecases.*
import pe.com.onecanal.presentation.ui.features.bank.intent.BankIntent
import pe.com.onecanal.presentation.ui.features.bank.state.BankIntentState
import pe.com.onecanal.presentation.ui.features.edit.intent.EditIntent
import pe.com.onecanal.presentation.ui.features.edit.state.EditIntentState
import pe.com.onecanal.presentation.ui.features.splash.intent.SplashIntent
import pe.com.onecanal.presentation.ui.util.FormField
import javax.inject.Inject

@HiltViewModel
class BankViewModel@Inject constructor(
    private val getBanksUseCase: GetBanksUseCase,
    private val saveBankAccountUseCase: SaveBankAccountUseCase,
    private val getUserDataFromRemoteUseCase: GetUserDataFromRemoteUseCase,
    private val saveUserSessionUseCase: SaveUserSessionUseCase,
    private val getUserSessionUseCase: GetUserSessionUseCase
) : ViewModel() {

    val number: FormField = FormField(
        emptyResourceId = R.string.required,
    )
    val cci: FormField = FormField(
        emptyResourceId = R.string.required,
    )
    val bankType: FormField = FormField(
        emptyResourceId = R.string.select_bank_type,
    )

    fun fieldsAreValid(): Boolean {
        if (!number.fieldIsValid() || !cci.fieldIsValid() || !bankType.fieldIsValid()) {
            return false
        }
        return true
    }

    val bankIntent = Channel<BankIntent>(Channel.UNLIMITED)

    private val _intentState = MutableStateFlow<BankIntentState>(BankIntentState.Idle)
    val intentState: StateFlow<BankIntentState> get() = _intentState

    init {
        observeUserIntent()
    }

    private fun observeUserIntent() {
        viewModelScope.launch {
            bankIntent.consumeAsFlow().collect {
                when (it) {
                    is BankIntent.SaveBankAccount -> doSaveBanks(
                        it.bankType,
                    )
                    is BankIntent.GetUserDataFromRemote -> getUserDataFromRemote()
                    is BankIntent.UpdateUserSession -> saveUserSession(it.userProfileData)
                    is BankIntent.GetUserSession -> getUserSession()
                    BankIntent.GetBanks -> getBanks()
                }
            }
        }
    }

    private fun getUserSession() {
        _intentState.value = BankIntentState.Loading
        getUserSessionUseCase.invoke(viewModelScope) { response ->
            response.either(
                { _intentState.value = BankIntentState.Error(it) },
                { _intentState.value = BankIntentState.GetUserSessionFromLocal(it) })
        }
    }

    private fun doSaveBanks(bankType: Int?) {
        if (fieldsAreValid()) {
            _intentState.value = BankIntentState.Loading
            saveBankAccountUseCase.invoke(
                viewModelScope,
                SaveBankAccountUseCase.Params(
                    bankType!!.toString(),
                    number.fieldText.value!!,
                    cci.fieldText.value!!
                )
            ) { response ->
                response.either(
                    { _intentState.value = BankIntentState.Error(it) },
                    { _intentState.value = BankIntentState.SaveBankAccount })
            }
        }
    }

    private fun getBanks() {
        _intentState.value = BankIntentState.Loading
        getBanksUseCase.invoke(viewModelScope) { response ->
            response.either(
                { _intentState.value = BankIntentState.Error(it) },
                {_intentState.value = BankIntentState.GetBanks(it) })
        }

    }

    private fun saveUserSession(userProfileData: UserProfileData) {
        _intentState.value = BankIntentState.Loading
        saveUserSessionUseCase.invoke(viewModelScope, userProfileData) { response ->
            response.either(
                { _intentState.value = BankIntentState.Error(it) },
                { _intentState.value = BankIntentState.UserSessionSaved })
        }
    }

    private fun getUserDataFromRemote() {
        _intentState.value = BankIntentState.Loading
        getUserDataFromRemoteUseCase.invoke(viewModelScope) { response ->
            response.either(
                { _intentState.value = BankIntentState.Error(it) },
                { _intentState.value = BankIntentState.UserDataFromRemoteFetched(it) })
        }
    }

}