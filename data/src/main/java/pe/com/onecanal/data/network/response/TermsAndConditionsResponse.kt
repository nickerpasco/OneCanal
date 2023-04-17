package pe.com.onecanal.data.network.response

data class TermsAndConditionsResponse(
    val created_at: String,
    val id: Int,
    val text: String,
    val type: String,
    val updated_at: String
)