package pe.com.onecanal.presentation.ui.features.salaryAdvance.stepThree.intent

import pe.com.onecanal.domain.entity.SalaryAdvanceDetails
import pe.com.onecanal.presentation.ui.base.BaseIntentCallback
import pe.com.onecanal.presentation.ui.features.salaryAdvance.stepThree.state.SalaryAdvanceStepThreeIntentState

/**
 * Created by Sergio Carrillo Diestra on 18/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
object SalaryAdvanceStepThreeIntentConfig {

    private lateinit var callback: IntentCallback

    fun instance(baseIntentCallback: IntentCallback): SalaryAdvanceStepThreeIntentConfig {
        callback = baseIntentCallback
        return this
    }

    fun collectIntentState(intentState: SalaryAdvanceStepThreeIntentState) {
        when (intentState) {
            SalaryAdvanceStepThreeIntentState.Idle -> {}
            is SalaryAdvanceStepThreeIntentState.Error -> callback.onError(intentState.error)
            SalaryAdvanceStepThreeIntentState.Loading -> callback.onLoading()
            SalaryAdvanceStepThreeIntentState.SaveSalaryAdvanceSuccessful -> callback.onSalaryAdvanceSavedSuccessFull()
            is SalaryAdvanceStepThreeIntentState.TermsAndConditionsFetched -> callback.onTermsAndConditionsFetched(intentState.terms)
        }
    }

    interface IntentCallback : BaseIntentCallback {
        fun onSalaryAdvanceSavedSuccessFull()
        fun onTermsAndConditionsFetched(terms:String)
    }
}