package pe.com.onecanal.data.network.response

import com.google.gson.annotations.SerializedName

data class ValidateResponse(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("email") val email: String
)