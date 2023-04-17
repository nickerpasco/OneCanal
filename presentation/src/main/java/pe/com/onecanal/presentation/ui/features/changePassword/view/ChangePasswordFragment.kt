package pe.com.onecanal.presentation.ui.features.changePassword.view

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import pe.com.onecanal.BR
import pe.com.onecanal.R
import pe.com.onecanal.databinding.FragmentChangePasswordBinding
import pe.com.onecanal.domain.entity.Failure
import pe.com.onecanal.presentation.ui.base.BaseFragmentWithViewModel
import pe.com.onecanal.presentation.ui.dialogs.MessageDialogType
import pe.com.onecanal.presentation.ui.features.changePassword.intent.ChangePasswordIntentConfig
import pe.com.onecanal.presentation.ui.features.changePassword.viewModel.ProfileChangePasswordViewModel
import pe.com.onecanal.presentation.ui.features.mainDrawer.view.MainDrawerActivity

@AndroidEntryPoint
class ChangePasswordFragment :
    BaseFragmentWithViewModel<FragmentChangePasswordBinding, ProfileChangePasswordViewModel>(),
    ChangePasswordIntentConfig.IntentCallback {

    override fun onCreateView(view: View) {
        binding.apply {
            appbar.tvTitle.text = getString(R.string.change_password)
            appbar.btnBack.setOnClickListener { goBack() }
            btnCancel.setOnClickListener {
                goBack()
            }
        }

        observeIntent()
    }

    override fun onResume() {
        super.onResume()
        //HIDE THE TOPBAR ACCORDING TO UI REQUIREMENTS
        (activity as MainDrawerActivity).showTopBar(false)
    }

    private fun observeIntent() {
        lifecycleScope.launchWhenCreated {
            viewModel.intentState.collect {
                ChangePasswordIntentConfig.instance(this@ChangePasswordFragment)
                    .collectIntentState(it)
            }
        }
    }

    override fun configureDataBinding(): FragmentChangePasswordBinding =
        FragmentChangePasswordBinding.inflate(layoutInflater)

    override fun configureViewModel(): ProfileChangePasswordViewModel {
        val viewModel: ProfileChangePasswordViewModel by viewModels()
        return viewModel
    }

    override val configureViewModelBindingVariable: Int
        get() = BR.ProfileChangePasswordViewModel

    override fun onPasswordChanged() {
        closeLoadingDialog()
        showMessageDialog(MessageDialogType.Success, getString(R.string.passwordUpdateSuccess)) {
            goBack()
        }
    }

    private fun goBack() {
        findNavController().popBackStack()
    }

    override fun onLoading() {
        showLoadingDialog()
    }

    override fun onError(error: Failure) {
        closeLoadingDialog()
        handleUseCaseFailureFromBase(error).message?.apply {
            showMessageDialog(MessageDialogType.Error, this)
        }
    }
}