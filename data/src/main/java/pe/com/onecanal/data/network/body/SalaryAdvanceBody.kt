package pe.com.onecanal.data.network.body

data class SalaryAdvanceBody(
    val account_id: Int,
    val amount: Double,
    val fees: String,
    val period_name: String,
    val reason_id: Int,
    val transfer_amount: String
)