package pe.com.onecanal.domain.utilsreniec

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("message") val message: String,
    @SerializedName("code") val code: Int,
    @SerializedName("description") val description: String
)