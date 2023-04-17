package pe.com.onecanal.presentation.ui.features.history.state

import pe.com.onecanal.domain.entity.Failure
import pe.com.onecanal.domain.entity.HistoryItem
import pe.com.onecanal.domain.entity.HistoryPagination
import pe.com.onecanal.domain.entity.UserProfileData

sealed class HistoryIntentState {
    object Loading : HistoryIntentState()
    data class Error(val error: Failure) : HistoryIntentState()
    data class GetUserSession(val userProfileData: UserProfileData?) : HistoryIntentState()
    data class GetHistory(val historyItem: HistoryPagination) : HistoryIntentState()
    object Idle : HistoryIntentState()
}
