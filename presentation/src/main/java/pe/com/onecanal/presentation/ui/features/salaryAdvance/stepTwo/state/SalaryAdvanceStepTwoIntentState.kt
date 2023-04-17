package pe.com.onecanal.presentation.ui.features.salaryAdvance.stepTwo.state

import pe.com.onecanal.domain.entity.Failure
import pe.com.onecanal.domain.entity.SalaryAdvanceDetails
import pe.com.onecanal.domain.entity.BankAccount

/**
 * Created by Sergio Carrillo Diestra on 18/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
sealed class SalaryAdvanceStepTwoIntentState {

    object Loading : SalaryAdvanceStepTwoIntentState()
    data class Error(val error: Failure) : SalaryAdvanceStepTwoIntentState()
    data class SalaryAdvanceDetailsFetched(val details: SalaryAdvanceDetails) : SalaryAdvanceStepTwoIntentState()
    data class BankAccountsFetched(val accounts: List<BankAccount>) : SalaryAdvanceStepTwoIntentState()
    object Idle : SalaryAdvanceStepTwoIntentState()
}