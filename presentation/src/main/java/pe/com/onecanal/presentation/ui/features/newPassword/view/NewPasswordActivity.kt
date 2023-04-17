package pe.com.onecanal.presentation.ui.features.newPassword.view

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pe.com.onecanal.BR
import pe.com.onecanal.R
import pe.com.onecanal.data.config.DataStoreConfig.ARGS_EMAIL_TOKEN
import pe.com.onecanal.databinding.ActivityNewPasswordBinding
import pe.com.onecanal.domain.entity.Failure
import pe.com.onecanal.domain.entity.ValidateData
import pe.com.onecanal.presentation.ui.base.BaseActivity
import pe.com.onecanal.presentation.ui.dialogs.MessageDialogType
import pe.com.onecanal.presentation.ui.extensions.startNewActivityClearStack
import pe.com.onecanal.presentation.ui.features.login.view.LoginActivity
import pe.com.onecanal.presentation.ui.features.newPassword.intent.NewPasswordIntent
import pe.com.onecanal.presentation.ui.features.newPassword.intent.NewPasswordIntentConfig
import pe.com.onecanal.presentation.ui.features.newPassword.intent.NewPasswordIntentConfig.NewPasswordIntentCallback
import pe.com.onecanal.presentation.ui.features.newPassword.viewmodel.NewPasswordViewModel
import pe.com.onecanal.presentation.ui.util.dataBinding

@AndroidEntryPoint
class NewPasswordActivity : BaseActivity<ActivityNewPasswordBinding, NewPasswordViewModel>(),
    View.OnClickListener,
    NewPasswordIntentCallback {
    private lateinit var emailValidationToken: String

    private lateinit var code: String
    private lateinit var validateData: ValidateData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = intent
        emailValidationToken = intent.getStringExtra(ARGS_EMAIL_TOKEN) ?: ""

        validateData = intent.getSerializableExtra("extra0") as ValidateData
        code = intent.getSerializableExtra("extra1") as String

        configureClickListeners()
    }

    private fun configureClickListeners() {
        binding.apply {
            saveNewPasswordBtn.setOnClickListener(this@NewPasswordActivity)
            cancelNewPassowordBtn.setOnClickListener(this@NewPasswordActivity)
        }
    }

    override suspend fun handleIntentStates() {
        viewModel.intentState.collect { NewPasswordIntentConfig.instance(this).collect(it) }
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.saveNewPasswordBtn -> saveNewPassword()
            R.id.cancelNewPassowordBtn -> finish()
        }
    }

    private fun saveNewPassword() {
        val password: String = binding.passwordEt.text.toString()
        val repeatedPassword: String = binding.passwordRepeatEt.text.toString()

        if (formIsValid(password, repeatedPassword)) {
            lifecycleScope.launch {
                viewModel.userIntent.send(
                    NewPasswordIntent.CreateNewPassowrd(
                        code,
                        validateData.email,
                        validateData.userId,
                        password,
                        password
                    )
                )
            }
        }
    }

    private fun formIsValid(password: String, repeatedPassword: String): Boolean {

        when {
            password.isEmpty() -> {
                showSnackBar(binding.root, resources.getString(R.string.password_must_not_empty))
                return false
            }
            repeatedPassword.isEmpty() -> {
                showSnackBar(binding.root, resources.getString(R.string.digit_repeat_password))
                return false
            }
            password != repeatedPassword -> {
                showSnackBar(binding.root, resources.getString(R.string.password_must_be_the_same))
                return false
            }
            password.length < 6 -> {
                showSnackBar(
                    binding.root,
                    resources.getString(R.string.password_min_characters_error)
                )
                return false
            }
            password.length < 6 || repeatedPassword.length < 6 -> {
                showSnackBar(binding.root, resources.getString(R.string.digit_repeat_password))
                return false
            }
            else -> return true
        }

    }

    override fun onNewPasswordCreated() {
        closeLoadingDialog()
        showMessageDialog(
            MessageDialogType.Success,
            resources.getString(R.string.account_activation_successful)
        ) {
            startNewActivityClearStack<LoginActivity>()
        }
    }

    override fun onLoading() {
        showLoadingDialog()
    }

    override fun onError(error: Failure) {
        closeLoadingDialog()
        val uiError = handleUseCaseFailureFromBase(error)
        uiError.message?.let {
            showMessageDialog(MessageDialogType.Error, it) {

            }
        }
    }

    override val dataBindingViewModel = BR.newPasswordViewModel
    override val viewModel: NewPasswordViewModel by viewModels()
    override val binding by dataBinding(ActivityNewPasswordBinding::inflate)
}
