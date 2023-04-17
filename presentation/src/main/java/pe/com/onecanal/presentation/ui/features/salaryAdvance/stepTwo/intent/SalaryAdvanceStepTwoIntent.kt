package pe.com.onecanal.presentation.ui.features.salaryAdvance.stepTwo.intent

/**
 * Created by Sergio Carrillo Diestra on 18/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
sealed class SalaryAdvanceStepTwoIntent {
    object GetBankAccounts : SalaryAdvanceStepTwoIntent()
    data class GetSalaryAdvanceDetails(val amount: Double) : SalaryAdvanceStepTwoIntent()

}