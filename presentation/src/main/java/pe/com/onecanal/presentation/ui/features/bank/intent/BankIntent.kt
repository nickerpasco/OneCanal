package pe.com.onecanal.presentation.ui.features.bank.intent

import pe.com.onecanal.domain.entity.UserProfileData

sealed class BankIntent {
    object GetBanks : BankIntent()
    class SaveBankAccount(val bankType: Int?) : BankIntent()
    object GetUserDataFromRemote : BankIntent()
    data class UpdateUserSession(val userProfileData: UserProfileData): BankIntent()
    object GetUserSession : BankIntent()
}
