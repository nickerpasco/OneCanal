package pe.com.onecanal.data.network.response

data class AccountResponse(
    val bank: BankResponse,
    val bank_id: Int,
    val id: Int,
    val number: String,
    val active: Boolean,
    var confirmed: Boolean
)