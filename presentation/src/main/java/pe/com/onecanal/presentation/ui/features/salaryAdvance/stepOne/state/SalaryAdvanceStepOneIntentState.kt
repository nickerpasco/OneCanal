package pe.com.onecanal.presentation.ui.features.salaryAdvance.stepOne.state

import pe.com.onecanal.domain.entity.*

/**
 * Created by Sergio Carrillo Diestra on 18/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
sealed class SalaryAdvanceStepOneIntentState {
    object Loading : SalaryAdvanceStepOneIntentState()
    data class Error(val error: Failure) : SalaryAdvanceStepOneIntentState()
    data class SalaryAdvanceReasonFetched(val reasons: List<SalaryAdvanceReason>) : SalaryAdvanceStepOneIntentState()
    data class FeesFetched(val data: FeeDomain) : SalaryAdvanceStepOneIntentState()
    data class SalaryFetched(val salary: Salary) : SalaryAdvanceStepOneIntentState()
    object SalarySwitchStateSaved : SalaryAdvanceStepOneIntentState()
    data class SalarySwitchStateFetched(val state: Boolean) : SalaryAdvanceStepOneIntentState()
    object Idle : SalaryAdvanceStepOneIntentState()

    data class UserSessionFromLocalFetched(val userProfileData: UserProfileData?) : SalaryAdvanceStepOneIntentState()
    data class UserDataFromRemoteFetched(val userProfileData: UserProfileData) : SalaryAdvanceStepOneIntentState()
    object UserSessionSaved: SalaryAdvanceStepOneIntentState()
}