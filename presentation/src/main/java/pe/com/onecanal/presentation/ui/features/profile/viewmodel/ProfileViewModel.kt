package pe.com.onecanal.presentation.ui.features.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import pe.com.onecanal.domain.entity.UserProfileData
import pe.com.onecanal.domain.usecases.GetUserDataFromRemoteUseCase
import pe.com.onecanal.domain.usecases.GetUserSessionUseCase
import pe.com.onecanal.domain.usecases.SaveUserSessionUseCase
import pe.com.onecanal.presentation.ui.features.profile.intent.ProfileIntent
import pe.com.onecanal.presentation.ui.features.profile.state.ProfileIntentState
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserSessionUseCase: GetUserSessionUseCase,
    private val getUserDataFromRemoteUseCase: GetUserDataFromRemoteUseCase,
    private val saveUserSessionUseCase: SaveUserSessionUseCase
) : ViewModel() {

    val userIntent = Channel<ProfileIntent>(Channel.UNLIMITED)
    private val _intentState = MutableStateFlow<ProfileIntentState>(ProfileIntentState.Idle)
    val intentState: StateFlow<ProfileIntentState> get() = _intentState

    init {
        observeUserIntent()
    }

    private fun observeUserIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    ProfileIntent.GetUserSessionFromLocal -> getUserSessionFromLocal()
                    ProfileIntent.GetUserDataFromRemote -> getUserDataFromRemote()
                    is ProfileIntent.UpdateUserSession -> saveUserSession(it.userProfileData)
                }
            }
        }
    }

    private fun saveUserSession(userProfileData: UserProfileData) {
        saveUserSessionUseCase.invoke(viewModelScope, userProfileData) { response ->
            response.either(
                { _intentState.value = ProfileIntentState.Error(it) },
                { _intentState.value = ProfileIntentState.UserSessionSaved })
        }
    }

    private fun getUserSessionFromLocal() {
        _intentState.value = ProfileIntentState.Loading
        getUserSessionUseCase.invoke(viewModelScope) { response ->
            response.either(
                { _intentState.value = ProfileIntentState.Error(it) },
                { _intentState.value = ProfileIntentState.UserSessionFromLocalFetched(it) })
        }
    }

    private fun getUserDataFromRemote() {
        getUserDataFromRemoteUseCase.invoke(viewModelScope) { response ->
            response.either(
                { _intentState.value = ProfileIntentState.Error(it) },
                { _intentState.value = ProfileIntentState.UserDataFromRemoteFetched(it) })
        }
    }

}
