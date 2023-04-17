package pe.com.onecanal.presentation.ui.features.salaryAdvance.stepThree.state

import pe.com.onecanal.domain.entity.Failure
import pe.com.onecanal.domain.entity.SalaryAdvanceDetails

/**
 * Created by Sergio Carrillo Diestra on 18/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
sealed class SalaryAdvanceStepThreeIntentState {

    object Loading : SalaryAdvanceStepThreeIntentState()
    data class Error(val error: Failure) : SalaryAdvanceStepThreeIntentState()
    data class TermsAndConditionsFetched(val terms: String) : SalaryAdvanceStepThreeIntentState()
    object SaveSalaryAdvanceSuccessful : SalaryAdvanceStepThreeIntentState()
    object Idle : SalaryAdvanceStepThreeIntentState()
}