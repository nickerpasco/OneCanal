package pe.com.onecanal.presentation.ui.features.history.intent

sealed class HistoryIntent {
    object GetUserSession : HistoryIntent()
    data class GetHistory(val page:Int) : HistoryIntent()
}
