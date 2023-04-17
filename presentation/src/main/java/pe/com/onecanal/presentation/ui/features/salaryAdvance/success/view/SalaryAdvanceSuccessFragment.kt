package pe.com.onecanal.presentation.ui.features.salaryAdvance.success.view

import android.view.View
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import pe.com.onecanal.R
import pe.com.onecanal.databinding.FragmentSalaryAdvanceSuccesBinding
import pe.com.onecanal.presentation.ui.base.BaseFragmentWithViewModel
import pe.com.onecanal.presentation.ui.extensions.toTwoDecimalStr
import pe.com.onecanal.presentation.ui.features.mainDrawer.view.MainDrawerActivity
import pe.com.onecanal.presentation.ui.features.mainDrawer.viewmodel.MainDrawerViewModel

@AndroidEntryPoint
class SalaryAdvanceSuccessFragment :
    BaseFragmentWithViewModel<FragmentSalaryAdvanceSuccesBinding, MainDrawerViewModel>(),
    View.OnClickListener {

    override fun onCreateView(view: View) {
        super.onCreateView(view)
        viewModel = ViewModelProvider(requireActivity())[MainDrawerViewModel::class.java]//  ViewModelProvider(requireParentFragment().requireParentFragment())[SalaryAdvanceViewModel::class.java]

//        viewModel.showStepperView.value = false
        (requireActivity() as OnBackPressedDispatcherOwner).onBackPressedDispatcher.addCallback(
            viewLifecycleOwner
        ) {
        }

        binding.apply {
            goBackToHomeTv.setOnClickListener(this@SalaryAdvanceSuccessFragment)
            configureUI()
        }
    }



    private fun FragmentSalaryAdvanceSuccesBinding.configureUI() {
        headerTv.text = getString(
            R.string.AdvanceRequestSuccesfullySend,
            viewModel.salaryAdvanceDetails?.date ?: ""
        )
        paymentPeriodTv.text = viewModel.salaryAdvanceDetails?.period_name ?: ""
        accountTv.text = getString(
            R.string.destination_account,
            "${viewModel.account?.shortName} - ${viewModel.account?.number}"
        )
        salaryAdvanceAmountTv.text =
            getString(R.string.advance_amount, viewModel.amount.toTwoDecimalStr())
        feesTv.text =
            getString(R.string.total_comissions, viewModel.salaryAdvanceDetails?.fees ?: "")
        amountToTransferTv.text = viewModel.salaryAdvanceDetails?.transfer_amount ?: ""
        SalaryAdvanceREasonTv.text =
            getString(R.string.advance_reason, viewModel.reason?.name ?: "")
    }

    private fun navigateToHomePage() {
        findNavController().navigate(SalaryAdvanceSuccessFragmentDirections.actionSalaryAdvanceSuccesFragmentToNavStepOne())

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.go_back_to_home_tv -> navigateToHomePage()
        }
    }

    override fun configureViewModel(): MainDrawerViewModel {
        val viewModel: MainDrawerViewModel by viewModels()
        return viewModel
    }

    override fun onResume() {
        super.onResume()
        (activity as MainDrawerActivity).showStepper(false)
    }

    override fun configureDataBinding() = FragmentSalaryAdvanceSuccesBinding.inflate(layoutInflater)
    override val configureViewModelBindingVariable: Int
        get() = R.layout.fragment_salary_advance_succes
}