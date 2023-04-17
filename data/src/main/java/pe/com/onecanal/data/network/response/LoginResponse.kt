package pe.com.onecanal.data.network.response

data class LoginResponse(
    val access_token: String,
    val me: MeResponse
)