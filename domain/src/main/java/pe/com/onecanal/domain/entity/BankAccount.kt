package pe.com.onecanal.domain.entity

data class BankAccount(
    var id: Int,
    var number: String,
    var shortName: String,
    var active: Boolean,
    var confirmed: Boolean
)