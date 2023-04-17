package pe.com.onecanal.presentation.ui.features.forgotPassoword.view

import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import pe.com.onecanal.BR
import pe.com.onecanal.R
import pe.com.onecanal.databinding.ActivityForgotPasswordBinding
import pe.com.onecanal.domain.entity.DocumentType
import pe.com.onecanal.domain.entity.Failure
import pe.com.onecanal.domain.entity.ValidateData
import pe.com.onecanal.presentation.ui.base.BaseActivity
import pe.com.onecanal.presentation.ui.dialogs.MessageDialogType
import pe.com.onecanal.presentation.ui.extensions.startActivityE
import pe.com.onecanal.presentation.ui.features.forgotPassoword.intent.ForgetPasswordIntent
import pe.com.onecanal.presentation.ui.features.forgotPassoword.intent.ForgotPasswordIntentConfig
import pe.com.onecanal.presentation.ui.features.forgotPassoword.viewmodel.ForgotPasswordViewModel
import pe.com.onecanal.presentation.ui.features.validateCode.view.CodeValidationActivity
import pe.com.onecanal.presentation.ui.util.dataBinding

@AndroidEntryPoint
class ForgotPasswordActivity :
    BaseActivity<ActivityForgotPasswordBinding, ForgotPasswordViewModel>(),
    ForgotPasswordIntentConfig.ForgotPasswordCallback {
    private var selectedDocumentType: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getDocumentTypes()
    }

    override suspend fun handleIntentStates() {
        viewModel.intentState.collect {
            ForgotPasswordIntentConfig.instance(this).handleStates(it)
        }
    }

    private fun getDocumentTypes() {
        viewModel.sendIntent(ForgetPasswordIntent.GetDocumentTypes)
    }

    override fun onDoForgotEventSuccess(data : ValidateData) {
        closeLoadingDialog()
        showMessageDialog(
            MessageDialogType.Info,
            stringRes(R.string.forgot_password_email_sended),
            isCancelable = false
        )
        {
            startActivityE<CodeValidationActivity>(data)
        }
    }

    override fun onDocumentTypesLoaded(data: List<DocumentType>) {
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

    override fun onCancelClicked() {
        finish()
    }

    override fun onSendButtonClicked() {
        viewModel.sendIntent(ForgetPasswordIntent.ForgetPasswordEvent)
    }

    override fun onLoading() {
        showLoadingDialog()
    }

    override fun onError(error: Failure) {
        closeLoadingDialog()
        val uiError = handleUseCaseFailureFromBase(error)
        uiError.message?.let { showMessageDialog(MessageDialogType.Error, it) {} }
    }

    override val dataBindingViewModel = BR.forgotPasswordViewModel
    override val viewModel: ForgotPasswordViewModel by viewModels()
    override val binding by dataBinding(ActivityForgotPasswordBinding::inflate)

}