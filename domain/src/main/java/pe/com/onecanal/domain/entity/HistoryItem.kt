package pe.com.onecanal.domain.entity

/**
 * Created by Sergio Carrillo Diestra on 27/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
data class HistoryItem(
    val account: String,
    val amount: String,
    val date: String,
    val periodName: String,
    val feesAmount: String,
    val reason: String,
    val status: SalaryAdvanceStatus,
    val transferAmount: String,
)