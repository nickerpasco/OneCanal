package pe.com.onecanal.presentation.ui.features.salaryAdvance.stepOne.intent

import pe.com.onecanal.domain.entity.*
import pe.com.onecanal.presentation.ui.base.BaseIntentCallback
import pe.com.onecanal.presentation.ui.features.salaryAdvance.stepOne.state.SalaryAdvanceStepOneIntentState

/**
 * Created by Sergio Carrillo Diestra on 18/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
object SalaryAdvanceStepOneIntentConfig {

    private lateinit var callback: IntentCallback

    fun instance(baseIntentCallback: IntentCallback): SalaryAdvanceStepOneIntentConfig {
        this.callback = baseIntentCallback
        return this
    }

    fun collectIntentState(intentState: SalaryAdvanceStepOneIntentState) {
        when (intentState) {
            is SalaryAdvanceStepOneIntentState.Error -> callback.onError(intentState.error)
            is SalaryAdvanceStepOneIntentState.FeesFetched -> callback.onFeesFetched(intentState.data)
            SalaryAdvanceStepOneIntentState.Loading -> callback.onLoading()
            is SalaryAdvanceStepOneIntentState.SalaryAdvanceReasonFetched -> callback.onSalaryAdvanceReasonsFetched(
                intentState.reasons
            )
            SalaryAdvanceStepOneIntentState.Idle -> {}
            is SalaryAdvanceStepOneIntentState.SalaryFetched -> {
                callback.onSalaryFetched(intentState.salary)
            }
            is SalaryAdvanceStepOneIntentState.SalarySwitchStateFetched -> callback.onSalarySwitchStateFetched(
                intentState.state
            )
            SalaryAdvanceStepOneIntentState.SalarySwitchStateSaved -> callback.onSalarySwitchStateSaved()
            is SalaryAdvanceStepOneIntentState.UserSessionFromLocalFetched -> callback.onUserDataFromLocal(intentState.userProfileData)
            SalaryAdvanceStepOneIntentState.UserSessionSaved -> callback.onUserSaved()
            is SalaryAdvanceStepOneIntentState.UserDataFromRemoteFetched -> callback.onUserDataFromService(intentState.userProfileData)
        }
    }

    interface IntentCallback : BaseIntentCallback {
        fun onFeesFetched(fees: FeeDomain)
        fun onSalaryAdvanceReasonsFetched(reasons: List<SalaryAdvanceReason>)
        fun onSalaryFetched(salary: Salary)
        fun onSalarySwitchStateFetched(state: Boolean)
        fun onSalarySwitchStateSaved()

        fun onUserDataFromLocal(userProfileData: UserProfileData?)
        fun onUserDataFromService(userProfileData: UserProfileData)
        fun onUserSaved()
    }
}