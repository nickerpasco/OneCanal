package pe.com.onecanal.presentation.ui.features.splash.viewModel

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
import pe.com.onecanal.presentation.ui.features.splash.intent.SplashIntent
import pe.com.onecanal.presentation.ui.features.splash.state.SplashIntentState
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getUserSessionUseCase: GetUserSessionUseCase,
    private val getUserDataFromRemoteUseCase: GetUserDataFromRemoteUseCase,
    private val saveUserSessionUseCase: SaveUserSessionUseCase
) :
    ViewModel() {

    val userIntent = Channel<SplashIntent>(Channel.UNLIMITED)

    private val _intentState = MutableStateFlow<SplashIntentState>(SplashIntentState.Idle)
    val intentState: StateFlow<SplashIntentState> get() = _intentState

    init {
        observeUserIntent()
    }

    private fun observeUserIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    SplashIntent.GetUserSessionFromLocal -> getUserSessionFromLocal()
                    SplashIntent.GetUserDataFromRemote -> getUserDataFromRemote()
                    is SplashIntent.UpdateUserSession -> saveUserSession(it.userProfileData)
                }
            }
        }
    }

    private fun saveUserSession(userProfileData: UserProfileData) {
        _intentState.value = SplashIntentState.Loading
        saveUserSessionUseCase.invoke(viewModelScope, userProfileData) { response ->
            response.either(
                { _intentState.value = SplashIntentState.Error(it) },
                { _intentState.value = SplashIntentState.UserSessionSaved })
        }
    }

    private fun getUserSessionFromLocal() {
        _intentState.value = SplashIntentState.Loading
        getUserSessionUseCase.invoke(viewModelScope) { response ->
            response.either(
                { _intentState.value = SplashIntentState.Error(it) },
                { _intentState.value = SplashIntentState.UserSessionFromLocalFetched(it) })
        }
    }

    private fun getUserDataFromRemote() {
        _intentState.value = SplashIntentState.Loading
        getUserDataFromRemoteUseCase.invoke(viewModelScope) { response ->
            response.either(
                { _intentState.value = SplashIntentState.Error(it) },
                { _intentState.value = SplashIntentState.UserDataFromRemoteFetched(it) })
        }
    }

}
