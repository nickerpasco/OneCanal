package pe.com.onecanal.framework.hilt.utilsreniec

import com.google.gson.annotations.SerializedName
import pe.com.onecanal.domain.utilsreniec.DataObj

data class ReponseDNI(
    @SerializedName("message") var message : String,
    @SerializedName("code") var code : Int,
    @SerializedName("data") var data : DataObj

)

