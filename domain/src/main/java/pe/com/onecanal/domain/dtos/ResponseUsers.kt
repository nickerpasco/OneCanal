package pe.com.onecanal.domain.dtos
import com.google.gson.annotations.SerializedName


data class ResponseUsers (

    @SerializedName("message") var message : String,
    @SerializedName("code") var code : Int,
    @SerializedName("data") var data : DataUserRes

)