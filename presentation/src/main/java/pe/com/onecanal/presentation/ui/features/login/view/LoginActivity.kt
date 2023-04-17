package pe.com.onecanal.presentation.ui.features.login.view

import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import pe.com.onecanal.BR
import pe.com.onecanal.R
import pe.com.onecanal.databinding.ActivityLoginBinding
import pe.com.onecanal.domain.entity.DocumentType
import pe.com.onecanal.domain.entity.Failure
import pe.com.onecanal.domain.entity.UserProfileData
import pe.com.onecanal.presentation.ui.base.BaseActivity
import pe.com.onecanal.presentation.ui.dialogs.MessageDialogType
import pe.com.onecanal.presentation.ui.extensions.startNewActivity
import pe.com.onecanal.presentation.ui.features.forgotPassoword.view.ForgotPasswordActivity
import pe.com.onecanal.presentation.ui.features.login.intent.LoginIntent
import pe.com.onecanal.presentation.ui.features.login.intent.LoginIntentConfig
import pe.com.onecanal.presentation.ui.features.login.viewmodel.LoginViewModel
import pe.com.onecanal.presentation.ui.features.mainDrawer.view.MainDrawerActivity
import pe.com.onecanal.presentation.ui.features.validateAccount.view.AccountValidationActivity
import pe.com.onecanal.presentation.ui.util.dataBinding

@AndroidEntryPoint
  class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>(),
    LoginIntentConfig.LoginIntentCallback {
    private var selectedDocumentType: String = ""
    private lateinit var userToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getDocumentTypes()
    }

    override suspend fun handleIntentStates() {
        viewModel.intentState.collect {
            LoginIntentConfig.instance(this@LoginActivity).handleStates(it)
        }
    }

    private fun getDocumentTypes() {
        viewModel.sendIntent(LoginIntent.GetDocumentTypes)
    }

    private fun goToValidateAccount() {
        startNewActivity<AccountValidationActivity>()
    }

    private fun goToForgotPassword() {
        startNewActivity<ForgotPasswordActivity>()
    }

    override fun onLoginSuccess(userProfileData: UserProfileData) {
        this.userToken = userProfileData.userToken
        viewModel.sendIntent(LoginIntent.SaveUserSession(userProfileData))
    }

    override fun onUserDataFromRemoteFetched(userProfileData: UserProfileData) {
        userProfileData.userToken = this.userToken
        viewModel.sendIntent(LoginIntent.SaveUserSessionAccount(userProfileData))
    }

    override fun onUserSessionSaved() {
        viewModel.sendIntent(LoginIntent.GetUserDataFromRemote)
    }

    override fun onUserSessionSavedAccount() {
        closeLoadingDialog()
        startNewActivity<MainDrawerActivity>()
    }

    override fun onDocumentTypesFetched(data: List<DocumentType>) {
        closeLoadingDialog()
        val adapter = ArrayAdapter(this, R.layout.list_popup_window_item, data)
        binding.apply {
            autocompleteDocumentTypes.setAdapter(adapter)
            autocompleteDocumentTypes.onItemClickListener =
                AdapterView.OnItemClickListener { _, _, pos, _ ->
                    selectedDocumentType = data[pos].name
                }
        }
    }

    override fun onAccountValidationClicked() {
        goToValidateAccount()
    }

    override fun onForgotPasswordClicked() {
        goToForgotPassword()
    }

    override fun onLoading() {
        showLoadingDialog()
    }

    override fun onError(error: Failure) {
        closeLoadingDialog()
        handleUseCaseFailureFromBase(error).message?.let {
            showMessageDialog(MessageDialogType.Error, it) {}
        }
    }

    override val dataBindingViewModel = BR.loginViewModel
    override val viewModel by viewModels<LoginViewModel>()
    override val binding by dataBinding(ActivityLoginBinding::inflate)

}