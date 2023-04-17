package pe.com.onecanal.presentation.ui.features.edit.viewModel

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
import pe.com.onecanal.domain.usecases.GetMaritalStatusUseCase
import pe.com.onecanal.domain.usecases.GetUserSessionUseCase
import pe.com.onecanal.domain.usecases.SaveProfileUseCase
import pe.com.onecanal.domain.usecases.SaveUserSessionUseCase
import pe.com.onecanal.presentation.ui.features.edit.intent.EditIntent
import pe.com.onecanal.presentation.ui.features.edit.state.EditIntentState
import pe.com.onecanal.presentation.ui.util.FormField
import javax.inject.Inject

@HiltViewModel
class EditViewModel@Inject constructor(
    private val getMaritalStatusUseCase: GetMaritalStatusUseCase,
    private val saveProfileUseCase: SaveProfileUseCase,
    private val saveUserSessionUseCase: SaveUserSessionUseCase,
    private val getUserSessionUseCase: GetUserSessionUseCase
) : ViewModel() {

//    val formFields = EditFormFields()

    val money: FormField = FormField(
        emptyResourceId = R.string.required,
    )
    val jobP: FormField = FormField(
        emptyResourceId = R.string.required,
    )
    val maritalType: FormField = FormField(
        emptyResourceId = R.string.select_marital_type,
    )
    val address: FormField = FormField(
        emptyResourceId = R.string.required,
    )

    fun fieldsAreValid(): Boolean {
        if (!money.fieldIsValid() || !maritalType.fieldIsValid() || !jobP.fieldIsValid() || !address.fieldIsValid()) {
            return false
        }
        return true
    }

    val userIntent = Channel<EditIntent>(Channel.UNLIMITED)

    private val _intentState = MutableStateFlow<EditIntentState>(EditIntentState.Idle)
    val intentState: StateFlow<EditIntentState> get() = _intentState

    init {
        observeUserIntent()
    }

    private fun observeUserIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is EditIntent.SaveProfile -> doSaveProfile(
                        it.maritalType,
                    )
                    EditIntent.GetMaritalStatus -> getMaritalStatusUseCase()
                    is EditIntent.UpdateUserSession -> saveUserSession(it.userProfileData)
                    is EditIntent.GetUserSession -> getUserSession()
                }
            }
        }
    }

    private fun getUserSession() {
        _intentState.value = EditIntentState.Loading
        getUserSessionUseCase.invoke(viewModelScope) { response ->
            response.either(
                { _intentState.value = EditIntentState.Error(it) },
                { _intentState.value = EditIntentState.GetUserSessionFromLocal(it) })
        }
    }

    private fun doSaveProfile(maritalStatus: Int?) {
        if (fieldsAreValid()) {
            maritalStatus?.let { m->
                _intentState.value = EditIntentState.Loading
                saveProfileUseCase.invoke(
                    viewModelScope,
                    SaveProfileUseCase.Params(
                        m,
                        address.fieldText.value!!,
                        jobP.fieldText.value!!,
                        if(money.enabled.value!!) money.fieldText.value!! else ""
                    )
                ) { response ->
                    response.either(
                        { _intentState.value = EditIntentState.Error(it) },
                        { _intentState.value = EditIntentState.SaveProfile(it) })
                }
            }
        }
    }

    private fun saveUserSession(userProfileData: UserProfileData) {
        _intentState.value = EditIntentState.Loading
        saveUserSessionUseCase.invoke(viewModelScope, userProfileData) { response ->
            response.either(
                { _intentState.value = EditIntentState.Error(it) },
                { _intentState.value = EditIntentState.UserSessionSaved })
        }
    }

    private fun getMaritalStatusUseCase() {
        _intentState.value = EditIntentState.Loading
        getMaritalStatusUseCase.invoke(viewModelScope) { response ->
            response.either(
                { _intentState.value = EditIntentState.Error(it) },
                {_intentState.value = EditIntentState.GetMaritalStatus(it) })
        }

    }
}