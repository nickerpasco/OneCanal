package pe.com.onecanal.data.network.body

data class LoginBody(
    val document_number: String,
    val document_type: String,
    val password: String
)