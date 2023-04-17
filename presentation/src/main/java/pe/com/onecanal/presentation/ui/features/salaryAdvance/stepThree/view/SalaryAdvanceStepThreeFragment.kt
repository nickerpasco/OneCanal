package pe.com.onecanal.presentation.ui.features.salaryAdvance.stepThree.view

import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
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
import pe.com.onecanal.databinding.FragmentSalaryAdvanceStepThreeBinding
import pe.com.onecanal.domain.entity.Failure
import pe.com.onecanal.domain.entity.SalaryAdvanceDetails
import pe.com.onecanal.domain.usecases.SaveSalaryAdvanceUseCase
import pe.com.onecanal.presentation.ui.base.BaseFragmentWithViewModel
import pe.com.onecanal.presentation.ui.dialogs.ConfirmationDialog
import pe.com.onecanal.presentation.ui.dialogs.MessageDialogType
import pe.com.onecanal.presentation.ui.dialogs.DocumentConfirmationDialog
import pe.com.onecanal.presentation.ui.features.mainDrawer.view.MainDrawerActivity
import pe.com.onecanal.presentation.ui.features.mainDrawer.viewmodel.MainDrawerViewModel
import pe.com.onecanal.presentation.ui.features.salaryAdvance.parentFragment.viewmodel.SalaryAdvanceViewModel
import pe.com.onecanal.presentation.ui.features.salaryAdvance.stepThree.intent.SalaryAdvanceStepThreeIntent
import pe.com.onecanal.presentation.ui.features.salaryAdvance.stepThree.viewModel.SalaryAdvanceStepThreeViewModel
import pe.com.onecanal.presentation.ui.features.salaryAdvance.stepThree.intent.SalaryAdvanceStepThreeIntentConfig

@AndroidEntryPoint
class SalaryAdvanceStepThreeFragment :
    BaseFragmentWithViewModel<FragmentSalaryAdvanceStepThreeBinding, SalaryAdvanceStepThreeViewModel>(),
    View.OnClickListener, SalaryAdvanceStepThreeIntentConfig.IntentCallback {
    private var termsAndConditions: String = ""
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
            btnContinueStpThree.setOnClickListener(this@SalaryAdvanceStepThreeFragment)
            btnCancelStpThree.setOnClickListener(this@SalaryAdvanceStepThreeFragment)
            termsAndConditionsTv.setOnClickListener(this@SalaryAdvanceStepThreeFragment)
            btnContinueStpThree.isEnabled = false
            createTermsAndConditionsSpannableText()
            checkBoxIn.isEnabled = false
            checkBox.setOnClickListener {
                openTermsDialog()
            }
        }
        observeIntent()

        lifecycleScope.launchWhenCreated {
            getTermsAndConditions()
        }
        printDataSalaryAdvance()
    }

    private fun printDataSalaryAdvance() {
        val details = parentViewModel.salaryAdvanceDetails!!
        binding.apply {
            amountAdvanceDateTv.text = getString(R.string.advance_amount_date)
            advanceAmountTv.text = details.amount
            feesTv.text = getString(R.string.total_comissions, details.fees)
            amountToTransferTv.text = details.transfer_amount
            paymentPeriodTv.text = details.period_name
            amountAdvanceDateTv.text = getString(R.string.advance_amount_date, details.date)

            parentViewModel.apply {
                advanceReasonTv.text = reason?.name ?: ""
                val shortName = account?.shortName ?: ""
                val number = account?.number ?: ""
                accountTv.text = "$shortName - $number"
            }
        }
    }

    private fun createTermsAndConditionsSpannableText() {
        val ss = SpannableString(getString(R.string.accept_money))
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {}
        }
        ss.setSpan(clickableSpan, 10, 31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.termsAndConditionsTv.text = ss
        binding.termsAndConditionsTv.movementMethod = LinkMovementMethod.getInstance();
    }

    private fun openTermsDialog() {
        DocumentConfirmationDialog(
            terms = termsAndConditions
        ) { action, _ ->
            binding.apply {
                checkBoxIn.isChecked = action
                btnContinueStpThree.isEnabled = action
            }
        }.show(
            parentFragmentManager,
            "termsAndConditionsDialog"
        )
    }

    private suspend fun getTermsAndConditions() {
        viewModel.userIntent.send(SalaryAdvanceStepThreeIntent.GetTermsAndConditions)
    }

    private fun observeIntent() {
        lifecycleScope.launchWhenCreated {
            viewModel.intentState.collect {
                SalaryAdvanceStepThreeIntentConfig.instance(this@SalaryAdvanceStepThreeFragment)
                    .collectIntentState(it)
            }
        }
    }

    private fun saveSalaryAdvance(params: SaveSalaryAdvanceUseCase.Params) {
        lifecycleScope.launch {
            viewModel.userIntent.send(
                SalaryAdvanceStepThreeIntent.SaveSalaryAdvance(params)
            )
        }
    }

    private fun showConfirmationDialog() {
        ConfirmationDialog(
            message = getString(R.string.wantToConfirSalaryAdvance),
            confirmationBtnTxt = getString(R.string.confirm),
            onButtonClick = {
                if (parentViewModel.dataForRequestIsValid()) {
                    val details = parentViewModel.salaryAdvanceDetails!!
                    saveSalaryAdvance(
                        SaveSalaryAdvanceUseCase.Params(
                            account_id = parentViewModel.account!!.id,
                            amount = parentViewModel.amount,
                            fees = details.fees,
                            period_name = details.period_name,
                            reason_id = parentViewModel.reason!!.id,
                            transfer_amount = details.transfer_amount
                        )
                    )
                } else {
                    showMessageDialog(MessageDialogType.Error, getString(R.string.error_general))
                }
            }
        ).show(parentFragmentManager, "SalaryAdvanceConfirmationDialog")
    }

    override fun onSalaryAdvanceSavedSuccessFull() {
        closeLoadingDialog()
        NavHostFragment.findNavController(requireParentFragment())
            .navigate(SalaryAdvanceStepThreeFragmentDirections.actionNavStepThreeToSalaryAdvanceSuccesFragment())
    }

    override fun onTermsAndConditionsFetched(terms: String) {
        closeLoadingDialog()
        this.termsAndConditions = terms
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_continue_stp_three -> showConfirmationDialog()
            R.id.btn_cancel_stp_three -> findNavController().navigate(
                SalaryAdvanceStepThreeFragmentDirections.actionNavStepThreeToNavStepOne()
            )
        }
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

    override fun configureViewModel(): SalaryAdvanceStepThreeViewModel {
        val viewModel: SalaryAdvanceStepThreeViewModel by viewModels()
        return viewModel
    }

    override fun configureDataBinding() =
        FragmentSalaryAdvanceStepThreeBinding.inflate(layoutInflater)

    override val configureViewModelBindingVariable: Int
        get() = BR.stepThreeViewModel

    override fun onResume() {
        super.onResume()
        (activity as MainDrawerActivity).showTopBar(true)
        parentViewModel.stepViewNumber.value = 3
    }

}