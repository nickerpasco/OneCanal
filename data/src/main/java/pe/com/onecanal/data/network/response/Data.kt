package pe.com.onecanal.data.network.response

data class Data(
    val account: Account,
    val amount: String,
    val date: String,
    val period_name: String,
    val fees_amount: String,
    val reason: Reason,
    val status: String,
    val transfer_amount: String,
    val id: Int
)