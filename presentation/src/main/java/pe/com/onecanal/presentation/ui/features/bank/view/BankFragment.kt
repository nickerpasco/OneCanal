package pe.com.onecanal.presentation.ui.features.bank.view

import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pe.com.onecanal.BR
import pe.com.onecanal.R
import pe.com.onecanal.databinding.FragmentBankBinding
import pe.com.onecanal.domain.entity.AccountBank
import pe.com.onecanal.domain.entity.Failure
import pe.com.onecanal.domain.entity.UserProfileData
import pe.com.onecanal.presentation.ui.base.BaseFragmentWithViewModel
import pe.com.onecanal.presentation.ui.bindingTools.AutoCompleteTextView.bindAdapter
import pe.com.onecanal.presentation.ui.dialogs.MessageDialogType
import pe.com.onecanal.presentation.ui.features.bank.intent.BankIntent
import pe.com.onecanal.presentation.ui.features.bank.intent.BankIntentConfig
import pe.com.onecanal.presentation.ui.features.bank.viewModel.BankViewModel
import pe.com.onecanal.presentation.ui.features.edit.intent.EditIntent
import pe.com.onecanal.presentation.ui.features.mainDrawer.view.MainDrawerActivity
import pe.com.onecanal.presentation.ui.features.splash.intent.SplashIntent

@AndroidEntryPoint
class BankFragment :
    BaseFragmentWithViewModel<FragmentBankBinding, BankViewModel>(),
    View.OnClickListener, BankIntentConfig.BankIntentCallback {

    var selectedType = ""
    val map : MutableMap<String, Int?> = mutableMapOf()
    private var userToken: String? = null

    override fun onCreateView(view: View) {
        binding.apply {
            appbar.tvTitle.text = getString(R.string.add_bank)
            appbar.btnBack.setOnClickListener { goBack() }
            btnCancel.setOnClickListener {
                goBack()
            }
            btnSave.setOnClickListener {
                saveProfile()
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.intentState.collect {
                BankIntentConfig.instance(this@BankFragment)
                    .collect(it)
            }
        }
        getBanks()
    }

    override fun onResume() {
        super.onResume()
        //HIDE THE TOPBAR ACCORDING TO UI REQUIREMENTS
        (activity as MainDrawerActivity).showTopBar(false)
    }

    private fun getBanks() {
        lifecycleScope.launchWhenCreated {
            viewModel.bankIntent.send(BankIntent.GetBanks)
            viewModel.bankIntent.send(BankIntent.GetUserSession)
        }
    }

    private fun saveProfile() {
        lifecycleScope.launchWhenCreated {
            viewModel.bankIntent.send(
                BankIntent.SaveBankAccount(
                bankType = map[selectedType]
            ))
        }
    }

    private fun goBack() {
        findNavController().popBackStack()
    }


    override fun configureViewModel(): BankViewModel {
        val viewModel by viewModels<BankViewModel>()
        return viewModel
    }

    override val configureViewModelBindingVariable: Int  get() = BR.bankViewModel

    override fun configureDataBinding(): FragmentBankBinding =
        FragmentBankBinding.inflate(layoutInflater)

    private fun adapterData(data : List<AccountBank>) {
        data.map {
            map[it.name] = it.id
        }
        closeLoadingDialog()
        binding.autocompleteBank.apply {
            bindAdapter(
                data.map { it.name },
                R.layout.list_popup_window_item,
                null
            )
            onItemClickListener =
                AdapterView.OnItemClickListener { _, _, pos, _ ->
                    selectedType = data[pos].name
                }
        }
    }

    override fun onClick(v: View?) {

    }

    override fun onGetBanksUseCase(data: List<AccountBank>) {
        adapterData(data)
    }

    override fun onSaveBankAccountUseCase() {
        closeLoadingDialog()
        showMessageDialog(
            dialogType = MessageDialogType.Success,
            message = getString(R.string.txt_save_bank),
            confirmationBtnTxt = getString(R.string.btn_save_success),
            isCancelable = false
        ) {
            this@BankFragment.userToken?.let {
                lifecycleScope.launch {
                    viewModel.bankIntent.send(BankIntent.GetUserDataFromRemote)
                }
            } ?: run {
                goBack()
            }
        }
    }

    override fun onUserDataFromRemoteFetched(userProfileData: UserProfileData) {
        userProfileData.userToken = this@BankFragment.userToken!!
        updateUserSession(userProfileData)
    }

    override fun onUserSessionFromLocalFetched(userProfileData: UserProfileData?) {
        this@BankFragment.userToken = userProfileData!!.userToken
    }

    private fun updateUserSession(userProfileData: UserProfileData) {
        lifecycleScope.launch {
            viewModel.bankIntent.send(BankIntent.UpdateUserSession(userProfileData))
        }
    }

    override fun onUserSessionSaved() {
        lifecycleScope.launch {
            goBack()
        }
    }

    override fun onLoading() {
        showLoadingDialog()
    }

    override fun onError(error: Failure) {
        closeLoadingDialog()
        handleUseCaseFailureFromBase(error).message?.let {
            showMessageDialog(MessageDialogType.Error, it)
        }
    }
//                if (selectedType.isEmpty() && cciEt.text!!.trim().isEmpty() && numberAccountEt.text!!.trim().isEmpty()) {
//                    showMessageDialog(
//                        dialogType = MessageDialogType.Info,
//                        message = getString(R.string.txt_incorrect_bank),
//                        confirmationBtnTxt = getString(R.string.btn_try_again)) {
//                        goBack()
//                    }
//                }
}