package pe.com.onecanal.data.network.response

data class SaveSalaryAdvanceResponse(
    val account: Account,
    val amount: String,
    val fees_amount: String,
    val id: Int,
    val period_name: String,
    val reason: Reason,
    val status: String,
    val transfer_amount: String
)