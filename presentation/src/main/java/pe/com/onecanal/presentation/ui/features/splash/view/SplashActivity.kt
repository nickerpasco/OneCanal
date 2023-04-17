package pe.com.onecanal.presentation.ui.features.splash.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pe.com.onecanal.BR
import pe.com.onecanal.databinding.ActivitySplashBinding
import pe.com.onecanal.domain.entity.Failure
import pe.com.onecanal.domain.entity.UserProfileData
import pe.com.onecanal.presentation.ui.base.BaseActivity
import pe.com.onecanal.presentation.ui.dialogs.MessageDialogType
import pe.com.onecanal.presentation.ui.extensions.startNewActivityClearStack
import pe.com.onecanal.presentation.ui.features.login.view.LoginActivity
import pe.com.onecanal.presentation.ui.features.mainDrawer.view.MainDrawerActivity
import pe.com.onecanal.presentation.ui.features.splash.intent.SplashIntent
import pe.com.onecanal.presentation.ui.features.splash.intent.SplashIntentConfig
import pe.com.onecanal.presentation.ui.features.splash.util.SplashUtil
import pe.com.onecanal.presentation.ui.features.splash.viewModel.SplashViewModel
import pe.com.onecanal.presentation.ui.util.dataBinding

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>(),
    SplashIntentConfig.SplashIntentCallback {
    private lateinit var userToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getUserSession()
        //MANAGE RESET PASSWORD DEEP LINK
        //SplashUtil.init(this) { getUserSession() }
    }

    override suspend fun handleIntentStates() {
        viewModel.intentState.collect {
            SplashIntentConfig.instance(this@SplashActivity).collectIntentState(it)
        }
    }

    private fun getUserSession() {
        lifecycleScope.launch {
            viewModel.userIntent.send(SplashIntent.GetUserSessionFromLocal)
        }
    }

    private fun getUserDataFromRemote() {
        lifecycleScope.launch {
            viewModel.userIntent.send(SplashIntent.GetUserDataFromRemote)
        }
    }

    private fun updateUserSession(userProfileData: UserProfileData) {
        lifecycleScope.launch {
            viewModel.userIntent.send(SplashIntent.UpdateUserSession(userProfileData))
        }
    }

    override fun onUserSessionFromLocalFetched(userProfileData: UserProfileData?) {
        if (userProfileData != null) {
            this.userToken = userProfileData.userToken
            getUserDataFromRemote()
        } else {
            startNewActivityClearStack<LoginActivity>()
        }
    }

    override fun onUserDataFromRemoteFetched(userProfileData: UserProfileData) {
        userProfileData.userToken = this.userToken
        updateUserSession(userProfileData)
    }

    override fun onUserSessionSaved() {
        startNewActivityClearStack<MainDrawerActivity>()
    }

    override fun onLoading() {
//        showLoadingDialog()
    }

    override fun onError(error: Failure) {
        closeLoadingDialog()
        handleUseCaseFailureFromBase(error).message?.let {
            showMessageDialog(MessageDialogType.Error, it) {
                when(error) {
                    is Failure.UnauthorizedOrForbidden -> if(error.code == 401) startNewActivityClearStack<LoginActivity>()
                    else -> {}
                }
            }
        }
    }

    override val dataBindingViewModel = BR.SplashViewModel
    override val viewModel: SplashViewModel by viewModels()
    override val binding by dataBinding(ActivitySplashBinding::inflate)

}