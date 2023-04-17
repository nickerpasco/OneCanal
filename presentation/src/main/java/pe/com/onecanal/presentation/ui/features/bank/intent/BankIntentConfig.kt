package pe.com.onecanal.presentation.ui.features.bank.intent

import pe.com.onecanal.domain.entity.AccountBank
import pe.com.onecanal.domain.entity.UserProfileData
import pe.com.onecanal.presentation.ui.base.BaseIntentCallback
import pe.com.onecanal.presentation.ui.features.bank.state.BankIntentState

object BankIntentConfig {

    private lateinit var callback: BankIntentCallback

    fun instance(BankIntentCallback: BankIntentCallback): BankIntentConfig {
        callback = BankIntentCallback
        return this
    }

    fun collect(intentState: BankIntentState) {
        when (intentState) {
            BankIntentState.Idle -> {}
            is BankIntentState.Error -> callback.onError(intentState.error)
            is BankIntentState.GetBanks -> callback.onGetBanksUseCase(
                intentState.data
            )
            BankIntentState.UserSessionSaved -> callback.onUserSessionSaved()
            BankIntentState.Loading -> callback.onLoading()
            BankIntentState.SaveBankAccount -> callback.onSaveBankAccountUseCase()
            is BankIntentState.UserDataFromRemoteFetched -> callback.onUserDataFromRemoteFetched(intentState.userProfileData)
            is BankIntentState.GetUserSessionFromLocal -> callback.onUserSessionFromLocalFetched(intentState.userProfileData)
        }
    }

    interface BankIntentCallback : BaseIntentCallback {
        fun onGetBanksUseCase(accounts: List<AccountBank>)
        fun onSaveBankAccountUseCase()
        fun onUserDataFromRemoteFetched(userProfileData: UserProfileData)
        fun onUserSessionFromLocalFetched(userProfileData: UserProfileData?)
        fun onUserSessionSaved()
    }

}