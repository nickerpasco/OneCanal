package pe.com.onecanal.presentation.ui.features.validateCode.view

import android.os.Bundle
import android.text.Editable
import android.view.KeyEvent
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import pe.com.onecanal.BR
import pe.com.onecanal.R
import pe.com.onecanal.databinding.ActivityCodeValidationBinding
import pe.com.onecanal.domain.entity.Failure
import pe.com.onecanal.domain.entity.ValidateData
import pe.com.onecanal.presentation.ui.base.BaseActivity
import pe.com.onecanal.presentation.ui.dialogs.MessageDialogType
import pe.com.onecanal.presentation.ui.extensions.getString
import pe.com.onecanal.presentation.ui.extensions.setFasterClickListener
import pe.com.onecanal.presentation.ui.extensions.startActivityE
import pe.com.onecanal.presentation.ui.features.newPassword.view.NewPasswordActivity
import pe.com.onecanal.presentation.ui.features.validateAccount.intent.AccountValidationIntent
import pe.com.onecanal.presentation.ui.features.validateAccount.intent.AccountValidationIntentConfig
import pe.com.onecanal.presentation.ui.features.validateCode.intent.CodeValidationIntent
import pe.com.onecanal.presentation.ui.features.validateCode.intent.CodeValidationIntentConfig
import pe.com.onecanal.presentation.ui.features.validateCode.viewmodel.CodeValidationViewModel
import pe.com.onecanal.presentation.ui.util.SimpleTextWatcher
import pe.com.onecanal.presentation.ui.util.dataBinding

@AndroidEntryPoint
class CodeValidationActivity : BaseActivity<ActivityCodeValidationBinding, CodeValidationViewModel>(), CodeValidationIntentConfig.CodeValidationIntentCallback {

    lateinit var validateData : ValidateData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        validateData = intent.getSerializableExtra("extra0") as ValidateData

        nextFocus(binding.first, binding.second)
        nextFocus(binding.second, binding.third)
        nextFocus(binding.third, binding.fourth)
        nextFocus(binding.fourth, null)
        setFocus(binding.first)
        setFocus(binding.second)
        setFocus(binding.third)
        setFocus(binding.fourth)

        binding.apply {
            appbar.tvTitle.text = getString(R.string.title_verification)
            appbar.btnBack.setFasterClickListener {
                finish()
            }
            btnValidateCode.setFasterClickListener {
                validCode()
            }
            txtCancelBack.setFasterClickListener {
                finish()
            }
        }
    }

    override suspend fun handleIntentStates() {
        viewModel.intentState.collect {
            CodeValidationIntentConfig.instance(intentCallback = this).collect(it)
        }
    }

    private fun validCode() {
        lifecycleScope.launchWhenCreated {
            viewModel.userIntent.send(CodeValidationIntent.ValidateCode(
                code = getLocalCode(),
                email = validateData.email,
                userId = validateData.userId
            ))
        }
    }

    override fun onBackPressed() {

    }

    private fun setFocus(input: AppCompatEditText) {
        input.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                if (input.text!!.length == 1) {
                    if (input.text.toString() == "") {
                        input.post {
                            input.setSelection(1)
                        }
                    }
                }
            }
        }
        input.setOnClickListener {
            if (input.text!!.length == 1) {
                input.post {
                    input.setSelection(1)
                }
            }
        }
    }

    @Suppress("DEPRECATED_IDENTITY_EQUALS")
    private fun nextFocus(input: AppCompatEditText, output: AppCompatEditText?) {
        input.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (event.action === KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (input == binding.first) {
                        binding.first.setText("")
                    }
                    if (input == binding.second) {
                        binding.first.requestFocus()
                        binding.second.setText("")
                        binding.first.setText("")
                    }
                    if (input == binding.third) {
                        binding.second.requestFocus()
                        binding.third.setText("")
                        binding.second.setText("")
                    }
                    if (input == binding.fourth) {
                        if (binding.fourth.text!!.trim().isEmpty()) {
                            binding.third.requestFocus()
                            binding.fourth.setText("")
                            binding.third.setText("")
                        }
                        else {
                            binding.fourth.setText("")
                        }
                    }
                    return@OnKeyListener true
                }
            }
            false
        })
        input.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                try {
                    when (s?.length) {
                        0 -> {
                        }
                        1 -> {
                            output?.let {
                                it.requestFocus()
                            }
                        }
                        else -> {
                            val last = s.toString().substring(1, 2)
                            s?.replace(0, 2, last)
                            output?.let {
                                it.requestFocus()
                            }
                        }
                    }
                    validateCode()
                } catch (ignore: Exception) {
                }
            }
        })
    }

    private fun validateCode() {
        val code = getLocalCode()
        binding.apply {
            btnValidateCode.isEnabled = code.length == 4
        }
    }

    private fun getLocalCode(): String {
        return binding.first.getString()+ binding.second.getString() + binding.third.getString() + binding.fourth.getString()
    }

    override fun onValidateCode() {
        startActivityE<NewPasswordActivity>(validateData, getLocalCode())
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

    override val dataBindingViewModel = BR.codeValidationViewModel
    override val viewModel: CodeValidationViewModel by viewModels()
    override val binding by dataBinding(ActivityCodeValidationBinding::inflate)
}