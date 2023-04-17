package pe.com.onecanal.data.network.body

import com.google.gson.annotations.SerializedName

data class ValidateTokenBody(
    @SerializedName("code") val code: String,
    @SerializedName("email") val email: String,
    @SerializedName("user_id") val userId: Int
)