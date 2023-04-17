package pe.com.onecanal.data.network.response

data class Account(
    val bank: Bank,
    val bank_id: Int,
    val id: Int,
    val number: String
)