package pe.com.onecanal.data.network.body

data class AccountValidationBody(
    val document_type: String,
    val document_number: String
)