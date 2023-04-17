package pe.com.onecanal.presentation.ui.features.salaryAdvance.stepTwo.view

import android.view.View
import android.widget.AdapterView
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pe.com.onecanal.BR
import pe.com.onecanal.R
import pe.com.onecanal.databinding.FragmentSalaryAdvanceStepTwoBinding
import pe.com.onecanal.domain.entity.BankAccount
import pe.com.onecanal.domain.entity.Failure
import pe.com.onecanal.domain.entity.SalaryAdvanceDetails
import pe.com.onecanal.presentation.ui.base.BaseFragmentWithViewModel
import pe.com.onecanal.presentation.ui.bindingTools.AutoCompleteTextView.bindAdapter
import pe.com.onecanal.presentation.ui.dialogs.MessageDialogType
import pe.com.onecanal.presentation.ui.features.mainDrawer.view.MainDrawerActivity
import pe.com.onecanal.presentation.ui.features.mainDrawer.viewmodel.MainDrawerViewModel
import pe.com.onecanal.presentation.ui.features.salaryAdvance.parentFragment.viewmodel.SalaryAdvanceViewModel
import pe.com.onecanal.presentation.ui.features.salaryAdvance.stepOne.intent.SalaryAdvanceStepOneIntent
import pe.com.onecanal.presentation.ui.features.salaryAdvance.stepThree.intent.SalaryAdvanceStepThreeIntent
import pe.com.onecanal.presentation.ui.features.salaryAdvance.stepTwo.intent.SalaryAdvanceStepTwoIntent
import pe.com.onecanal.presentation.ui.features.salaryAdvance.stepTwo.intent.SalaryAdvanceStepTwoIntentConfig
import pe.com.onecanal.presentation.ui.features.salaryAdvance.stepTwo.viewModel.SalaryAdvanceStepTwoViewModel

@AndroidEntryPoint
class SalaryAdvanceStepTwoFragment :
    BaseFragmentWithViewModel<FragmentSalaryAdvanceStepTwoBinding, SalaryAdvanceStepTwoViewModel>(),
    View.OnClickListener, SalaryAdvanceStepTwoIntentConfig.IntentCallback {

    private var selectedBankAccount: BankAccount? = null
    private lateinit var parentViewModel: MainDrawerViewModel

    override fun onCreateView(view: View) {
        super.onCreateView(view)
        (requireActivity() as OnBackPressedDispatcherOwner).onBackPressedDispatcher.addCallback(
            viewLifecycleOwner
        ) {
            NavHostFragment.findNavController(requireParentFragment()).popBackStack()
        }
        parentViewModel = ViewModelProvider(requireActivity())[MainDrawerViewModel::class.java]// ViewModelProvider(requireParentFragment().requireParentFragment())[SalaryAdvanceViewModel::class.java]

        binding.apply {
            btnContinueStpTwo.setOnClickListener(this@SalaryAdvanceStepTwoFragment)
            btnCancelStpTwo.setOnClickListener(this@SalaryAdvanceStepTwoFragment)
        }

        observeIntent()
        getBankAccounts()
    }

    private fun observeIntent() {
        lifecycleScope.launchWhenCreated {
            viewModel.intentState.collect {
                SalaryAdvanceStepTwoIntentConfig.instance(this@SalaryAdvanceStepTwoFragment)
                    .collectIntentState(it)
            }
        }
    }

    private fun getBankAccounts() {
        lifecycleScope.launchWhenCreated {
            viewModel.userIntent.send(SalaryAdvanceStepTwoIntent.GetBankAccounts)
        }
    }

    private fun cancel() {
        findNavController().navigate(SalaryAdvanceStepTwoFragmentDirections.actionNavStepTwoToNavStepOne())
    }

    private fun getSalaryAdvanceDetails() {
        if (viewModel.fieldsAreValid() && selectedBankAccount != null) {
            lifecycleScope.launch {
                viewModel.userIntent.send(SalaryAdvanceStepTwoIntent.GetSalaryAdvanceDetails(parentViewModel.amount))
            }
        }
    }

    private fun navigateToStepThree() {
        //SET ACCOUNT
        parentViewModel.account = selectedBankAccount
        NavHostFragment.findNavController(requireParentFragment()).navigate(SalaryAdvanceStepTwoFragmentDirections.actionNavStepTwoToNavStepThree())
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_continue_stp_two -> getSalaryAdvanceDetails()
            R.id.btn_cancel_stp_two -> cancel()
        }
    }
    @Suppress("LocalVariableName")
    override fun onBankAccountsFetched(accounts: List<BankAccount>) {
        val _account = accounts.filter { it.active && it.confirmed }
        closeLoadingDialog()
        binding.bankAccountAt.apply {
            bindAdapter(
                _account.map { "${it.shortName} - ${it.number}" },
                R.layout.list_popup_window_item,
                null
            )
            onItemClickListener =
                AdapterView.OnItemClickListener { _, _, pos, _ ->
                    selectedBankAccount = _account[pos]
                }
        }
    }

    override fun onSalaryAdvanceDetailsFetched(details: SalaryAdvanceDetails) {
        //SET DETAILS SALARY ADVANCE
        parentViewModel.salaryAdvanceDetails = details
        navigateToStepThree()
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

    override fun configureViewModel(): SalaryAdvanceStepTwoViewModel {
        val viewModel: SalaryAdvanceStepTwoViewModel by viewModels()
        return viewModel
    }

    override fun configureDataBinding() =
        FragmentSalaryAdvanceStepTwoBinding.inflate(layoutInflater)

    override fun onResume() {
        super.onResume()
        (activity as MainDrawerActivity).showTopBar(true)
        parentViewModel.stepViewNumber.value = 2
    }

    override val configureViewModelBindingVariable = BR.stepTwoViewModel

}