package pe.com.onecanal.data.network.body

import com.google.gson.annotations.SerializedName

data class ValidatePasswordBody(
    @SerializedName("code") val code: String,
    @SerializedName("email") val email: String,
    @SerializedName("user_id") val useId: Int,
    @SerializedName("password") val password: String,
    @SerializedName("password_confirmation") val password_confirmation: String
)