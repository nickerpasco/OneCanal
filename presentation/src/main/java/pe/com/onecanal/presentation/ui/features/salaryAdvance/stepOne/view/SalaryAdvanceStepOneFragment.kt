package pe.com.onecanal.presentation.ui.features.salaryAdvance.stepOne.view

import android.text.Editable
import android.view.View
import android.widget.AdapterView
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.activity.addCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pe.com.onecanal.BR
import pe.com.onecanal.R
import pe.com.onecanal.databinding.FragmentSalaryAdvanceStepOneBinding
import pe.com.onecanal.domain.entity.*
import pe.com.onecanal.presentation.ui.base.BaseFragmentWithViewModel
import pe.com.onecanal.presentation.ui.bindingTools.AutoCompleteTextView.bindAdapter
import pe.com.onecanal.presentation.ui.dialogs.MessageDialogType
import pe.com.onecanal.presentation.ui.extensions.*
import pe.com.onecanal.presentation.ui.features.mainDrawer.view.MainDrawerActivity
import pe.com.onecanal.presentation.ui.features.mainDrawer.viewmodel.MainDrawerViewModel
import pe.com.onecanal.presentation.ui.features.salaryAdvance.stepOne.intent.SalaryAdvanceStepOneIntent
import pe.com.onecanal.presentation.ui.features.salaryAdvance.stepOne.intent.SalaryAdvanceStepOneIntentConfig
import pe.com.onecanal.presentation.ui.features.salaryAdvance.stepOne.viewModel.SalaryAdvanceStepOneViewModel

@AndroidEntryPoint
class SalaryAdvanceStepOneFragment : BaseFragmentWithViewModel<FragmentSalaryAdvanceStepOneBinding, SalaryAdvanceStepOneViewModel>(), SalaryAdvanceStepOneIntentConfig.IntentCallback {
    private lateinit var fees: FeeDomain
    private lateinit var userSalary: Salary
    var accounts: List<BankAccount> = arrayListOf()
    private var selectedSalaryAdvanceReason: SalaryAdvanceReason? = null
    private lateinit var parentViewModel: MainDrawerViewModel
    private lateinit var userToken: String
    private var safeSecondCall = true
    private var oneDialog = true

    override fun onCreateView(view: View) {
        observeIntent()
        lifecycleScope.launchWhenCreated {
            getFees()
            getSalaryAdvanceReasons()
            getSalary()
            getSalarySwitchState()
            getUserSessionLocal()
        }
        (requireActivity() as OnBackPressedDispatcherOwner).onBackPressedDispatcher.addCallback(
            viewLifecycleOwner
        ) {}
        parentViewModel = ViewModelProvider(requireActivity())[MainDrawerViewModel::class.java]// ViewModelProvider(requireParentFragment().requireParentFragment())[SalaryAdvanceViewModel::class.java]
        parentViewModel.showStepperView.value = true

        binding.apply {
            btnContinueStpOne.setFasterClickListener {
                navigateToStepTwo()
            }
            availableSalarySw.setOnCheckedChangeListener { _, status ->
                if (status) salaryEtOt.visibility = View.VISIBLE
                else salaryEtOt.visibility = View.GONE
                saveSalarySwitchState(status)
            }
            salaryAdvanceAvailableEt.addTextChangedListener { text ->
                onSalaryTextChanged(text)
            }
        }
    }

    private fun getUserDataService() {
        lifecycleScope.launch {
            viewModel.userIntent.send(SalaryAdvanceStepOneIntent.GetUserDataFromRemote)
        }
    }

    private fun getUserSessionLocal() {
        lifecycleScope.launch {
            viewModel.userIntent.send(SalaryAdvanceStepOneIntent.GetUserSessionFromLocal)
        }
    }

    private fun saveUserData(userProfileData: UserProfileData) {
        lifecycleScope.launch {
            viewModel.userIntent.send(SalaryAdvanceStepOneIntent.UpdateUserSession(userProfileData))
        }
    }

    private fun onSalaryTextChanged(text: Editable?) {

        if (text.toString().isNotEmpty()) {
            viewModel.onSalaryAdvanceTextChanged(fees.feeRanges.value, text.toString()).let { fee ->
                if (fee != 0.00) {
                    fee.toTwoDecimalStr().let {

                        val inputValue = text.toString().toDouble()
                        val igv = fee * fees.igv.value / 100
                        val itfTxt =
                            (((inputValue - fee -igv) *fees.itf.value)/ 100).toFourDecimalStr()
                        binding.itftv.text = getString(R.string.itf, itfTxt)
                        binding.feeTv.text = getString(R.string.fee, it)
                        val igvTxt = igv.toTwoDecimalStr()
                        binding.igvTv.text = getString(R.string.igv, igvTxt)
                    }
                } else {
                    resetFeesView()
                }

            }
        } else {
            resetFeesView()
        }
    }

    private fun resetFeesView() {
        binding.feeTv.text = getString(R.string.fee).replace("%s", "-")
        binding.igvTv.text = getString(R.string.igv).replace("%s", "-")
        binding.itftv.text = getString(R.string.itf).replace("%s", "-")
    }

    private fun saveSalarySwitchState(status: Boolean) {
        lifecycleScope.launch {
            viewModel.userIntent.send(SalaryAdvanceStepOneIntent.SaveSalarySwitchState(status))
        }
    }

    private suspend fun getSalarySwitchState() {
        viewModel.userIntent.send(SalaryAdvanceStepOneIntent.GetSalarySwitchState)
    }

    private suspend fun getSalary() {
        viewModel.userIntent.send(SalaryAdvanceStepOneIntent.GetSalary)
    }

    private fun observeIntent() {
        lifecycleScope.launchWhenCreated {
            viewModel.intentState.collect {
                SalaryAdvanceStepOneIntentConfig.instance(this@SalaryAdvanceStepOneFragment).collectIntentState(it)
            }
        }
    }

    private suspend fun getFees() {
        viewModel.userIntent.send(SalaryAdvanceStepOneIntent.GetFees)
    }

    private suspend fun getSalaryAdvanceReasons() {
        viewModel.userIntent.send(SalaryAdvanceStepOneIntent.GetSalaryAdvanceReasons)
    }

    private fun navigateToStepTwo() {
        if (viewModel.fieldsAreValid() && selectedSalaryAdvanceReason != null) {
            //SET AMOUNT
            parentViewModel.amount = viewModel.availableSalaryAmountField.fieldText.value?.toDoubleWithTwoDecimals() ?: 0.0
            //SET REASON
            parentViewModel.reason = selectedSalaryAdvanceReason
            //GET USER DATA LOCAL AND RELOAD BY SERVICE
            getUserDataService()
        }
    }

    override fun onUserDataFromService(userProfileData: UserProfileData) {
        userProfileData.userToken = this.userToken
        this.accounts = userProfileData.accounts
        saveUserData(userProfileData)
    }

    override fun onUserSaved() {
        closeLoadingDialog()
        if (safeSecondCall) {
            safeSecondCall = false
            if (accounts.any { it.active && it.confirmed }) {
                findNavController().navigate(SalaryAdvanceStepOneFragmentDirections.actionStepOneToStepTwo())
            }
            else {
                dialogAccountsValid()
            }
        }
    }

    private fun dialogAccountsValid() {
        val isEmpty = accounts.isEmpty()
        val countInactive = accounts.filter { !it.active }.size
        val count = accounts.size
        val oneRevision = accounts.any { !it.confirmed }
        val oneActive = accounts.any { it.active && it.confirmed }
        if ((isEmpty || (countInactive == count)) && !oneRevision) {
            showMessageDialog(
                dialogType = MessageDialogType.Answer,
                message = getString(R.string.addBank),
                isCancelable = false,
                confirmationBtnTxt = getString(R.string.go)
            ) {
                findNavController().navigate(R.id.bankFragment)
            }
        }
        else if (oneRevision && !oneActive) {
            showMessageDialog(
                dialogType = MessageDialogType.Answer,
                message = getString(R.string.bankInRevision),
                isCancelable = false,
                confirmationBtnTxt = getString(R.string.ok)
            ) {
                safeSecondCall = true
            }
        }
    }

    override fun onFeesFetched(fees: FeeDomain) {
        closeLoadingDialog()
        this.fees = fees
        binding.apply {
            igvTv.text = getString(R.string.igv, " -")
            itftv.text = getString(R.string.itf, " -")
            binding.feeTv.text = getString(R.string.fee, " -")

        }
    }

    override fun onSalaryAdvanceReasonsFetched(reasons: List<SalaryAdvanceReason>) {
        closeLoadingDialog()
        binding.salaryAdvanceReasonAt.apply {
            bindAdapter(reasons.map { it.name }, R.layout.list_popup_window_item, null)
            onItemClickListener =
                AdapterView.OnItemClickListener { _, _, pos, _ ->
                    selectedSalaryAdvanceReason = reasons[pos]
                }
        }
    }

    override fun onSalaryFetched(salary: Salary) {
        closeLoadingDialog()
        this.userSalary = salary
        binding.salaryEtOt.editText?.setText(salary.salary.showAsCurrency())

        binding.labeltvTitle.text = getString(R.string.salary_advance_available_title)

        binding.availableSalaryTv.text = getString(
            R.string.salaryspace,
            salary.salaryAdvanceAvailable.toTwoDecimalStr()
        )
        viewModel.apply {
            availableSalaryAmountField.also {
                it.setMaxValue(salary.salaryAdvanceAvailable.toDoubleWithTwoDecimals())
            }
        }
    }

    override fun onSalarySwitchStateFetched(state: Boolean) {
        closeLoadingDialog()
        binding.apply {
            availableSalarySw.isChecked = state
        }
    }

    override fun onSalarySwitchStateSaved() {
        closeLoadingDialog()
    }

    override fun onUserDataFromLocal(userProfileData: UserProfileData?) {
        this.userToken = userProfileData?.userToken!!
        if (oneDialog) {
            oneDialog = false
            if (userProfileData.maritalId == null ||
                userProfileData.address.isNullOrEmpty() ||
                userProfileData.businessJob.isNullOrEmpty() ||
                userProfileData.salary == 0.0) showMessageDialog(
                dialogType = MessageDialogType.Answer,
                message = getString(R.string.incompleteProfile),
                isCancelable = false,
                confirmationBtnTxt = getString(R.string.go)
            ) {
                findNavController().navigate(R.id.editFragment)
            }
        }
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

    override fun onResume() {
        super.onResume()
        (activity as MainDrawerActivity).showTopBar(true)
        binding.salaryAdvanceAvailableEt.setText("")
        binding.salaryAdvanceReasonAt.text = null
        binding.salaryAdvanceReasonAt.isFocusable = false
        parentViewModel.stepViewNumber.value = 1
        safeSecondCall = true
        oneDialog = true
    }

    override fun configureViewModel(): SalaryAdvanceStepOneViewModel {
        val viewModel by viewModels<SalaryAdvanceStepOneViewModel>()
        return viewModel
    }

    override fun configureDataBinding() =
        FragmentSalaryAdvanceStepOneBinding.inflate(layoutInflater)

    override val configureViewModelBindingVariable = BR.stepOneViewmodel

}