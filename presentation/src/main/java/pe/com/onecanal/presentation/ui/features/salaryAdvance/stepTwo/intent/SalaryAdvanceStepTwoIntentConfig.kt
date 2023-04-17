package pe.com.onecanal.presentation.ui.features.salaryAdvance.stepTwo.intent

import pe.com.onecanal.domain.entity.BankAccount
import pe.com.onecanal.domain.entity.SalaryAdvanceDetails
import pe.com.onecanal.presentation.ui.base.BaseIntentCallback
import pe.com.onecanal.presentation.ui.features.salaryAdvance.stepTwo.state.SalaryAdvanceStepTwoIntentState

/**
 * Created by Sergio Carrillo Diestra on 18/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
object SalaryAdvanceStepTwoIntentConfig {

    private lateinit var callback: IntentCallback

    fun instance(baseIntentCallback: IntentCallback): SalaryAdvanceStepTwoIntentConfig {
        callback = baseIntentCallback
        return this
    }

    fun collectIntentState(intentState: SalaryAdvanceStepTwoIntentState) {
        when (intentState) {
            is SalaryAdvanceStepTwoIntentState.Error -> callback.onError(intentState.error)
            SalaryAdvanceStepTwoIntentState.Loading -> callback.onLoading()
            SalaryAdvanceStepTwoIntentState.Idle -> {}
            is SalaryAdvanceStepTwoIntentState.SalaryAdvanceDetailsFetched -> callback.onSalaryAdvanceDetailsFetched(
                intentState.details
            )
            is SalaryAdvanceStepTwoIntentState.BankAccountsFetched -> callback.onBankAccountsFetched(
                intentState.accounts
            )
        }
    }

    interface IntentCallback : BaseIntentCallback {
        fun onBankAccountsFetched(accounts: List<BankAccount>)
        fun onSalaryAdvanceDetailsFetched(details: SalaryAdvanceDetails)

    }
}