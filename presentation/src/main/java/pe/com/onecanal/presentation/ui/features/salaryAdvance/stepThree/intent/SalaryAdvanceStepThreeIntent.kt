package pe.com.onecanal.presentation.ui.features.salaryAdvance.stepThree.intent

import pe.com.onecanal.domain.usecases.SaveSalaryAdvanceUseCase

/**
 * Created by Sergio Carrillo Diestra on 18/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
sealed class SalaryAdvanceStepThreeIntent {
    data class SaveSalaryAdvance(val params: SaveSalaryAdvanceUseCase.Params) :
        SalaryAdvanceStepThreeIntent()
    object GetTermsAndConditions : SalaryAdvanceStepThreeIntent()
}