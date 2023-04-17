package pe.com.onecanal.presentation.ui.features.history.intent

import pe.com.onecanal.domain.entity.HistoryPagination
import pe.com.onecanal.presentation.ui.base.BaseIntentCallback
import pe.com.onecanal.presentation.ui.features.history.state.HistoryIntentState

/**
 * Created by Sergio Carrillo Diestra on 19/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
object HistoryIntentConfig {

    private lateinit var callback: IntentCallback

    fun instance(callback: IntentCallback): HistoryIntentConfig {
        HistoryIntentConfig.callback = callback
        return this
    }

    fun collect(intentState: HistoryIntentState) {
        when (intentState) {
            HistoryIntentState.Idle -> {}
            is HistoryIntentState.Error -> callback.onError(intentState.error)
            HistoryIntentState.Loading -> callback.onLoading()
            is HistoryIntentState.GetHistory -> callback.onHistoryFetched(intentState.historyItem)
            is HistoryIntentState.GetUserSession -> {}
        }
    }

    interface IntentCallback : BaseIntentCallback {
        fun onHistoryFetched(historyPagination: HistoryPagination)
    }

}