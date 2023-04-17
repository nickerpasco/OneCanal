package pe.com.onecanal.presentation.ui.features.salaryAdvance.stepOne.intent

import pe.com.onecanal.domain.entity.UserProfileData

/**
 * Created by Sergio Carrillo Diestra on 18/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
sealed class SalaryAdvanceStepOneIntent {
    object GetSalaryAdvanceReasons : SalaryAdvanceStepOneIntent()
    object GetFees : SalaryAdvanceStepOneIntent()
    object GetSalary : SalaryAdvanceStepOneIntent()
    object GetSalarySwitchState : SalaryAdvanceStepOneIntent()
    data class SaveSalarySwitchState(val state: Boolean) : SalaryAdvanceStepOneIntent()

    object GetUserSessionFromLocal : SalaryAdvanceStepOneIntent()
    object GetUserDataFromRemote : SalaryAdvanceStepOneIntent()
    data class UpdateUserSession(val userProfileData: UserProfileData): SalaryAdvanceStepOneIntent()

}