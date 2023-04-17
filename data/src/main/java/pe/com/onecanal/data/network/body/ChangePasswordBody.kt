package pe.com.onecanal.data.network.body

data class ChangePasswordBody(
    val current_password: String,
    val password: String,
    val password_confirmation: String
)