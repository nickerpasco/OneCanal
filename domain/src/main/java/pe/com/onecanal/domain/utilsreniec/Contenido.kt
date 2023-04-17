package pe.com.onecanal.domain.utilsreniec

import com.google.gson.annotations.SerializedName

data class Contenido (

    @SerializedName("apPrimer") var apPrimer : String,
    @SerializedName("apSegundo") var apSegundo : String,
    @SerializedName("direccion") var direccion : String,
    @SerializedName("estadoCivil") var estadoCivil : String,
    @SerializedName("prenombres") var prenombres : String,
    @SerializedName("nombrecompleto") var nombrecompleto : String,
    @SerializedName("email") var email : String,
    @SerializedName("fax") var fax : String,
    @SerializedName("telefono") var telefono : String,
    @SerializedName("celular") var celular : String,
    @SerializedName("ubigeo") var ubigeo : String,
    @SerializedName("sexo") var sexo : String,
    @SerializedName("Exito") var Exito : Boolean,
    @SerializedName("CodigoError") var CodigoError : String,
    @SerializedName("MensajeProceso") var MensajeProceso : String,
    @SerializedName("DatosAdicionales") var DatosAdicionales : String

)