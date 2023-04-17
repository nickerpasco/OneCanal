package pe.com.onecanal.presentation.ui.features.bank.state

import pe.com.onecanal.domain.entity.AccountBank
import pe.com.onecanal.domain.entity.Failure
import pe.com.onecanal.domain.entity.UserProfileData

sealed class BankIntentState {
    object Loading : BankIntentState()
    data class Error(val error: Failure) : BankIntentState()
    object SaveBankAccount : BankIntentState()
    data class GetBanks(val data: List<AccountBank>) : BankIntentState()
    object UserSessionSaved: BankIntentState()
    data class UserDataFromRemoteFetched(val userProfileData: UserProfileData) : BankIntentState()
    data class GetUserSessionFromLocal(val userProfileData: UserProfileData?) : BankIntentState()
    object Idle : BankIntentState()

}