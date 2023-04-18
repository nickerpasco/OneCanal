package pe.com.onecanal.domain.utilsreniec

import com.google.gson.annotations.SerializedName

data class Contenido (

    @SerializedName("numero") var numero : String,
    @SerializedName("nombre_completo") var nombreCompleto : String,
    @SerializedName("nombres") var nombres : String,
    @SerializedName("apellido_paterno") var apellidoPaterno : String,
    @SerializedName("apellido_materno") var apellidoMaterno : String,
    @SerializedName("codigo_verificacion") var codigoVerificacion : Int,
    @SerializedName("fecha_nacimiento") var fechaNacimiento : String,
    @SerializedName("sexo") var sexo : String,
    @SerializedName("estado_civil") var estadoCivil : String,
    @SerializedName("departamento") var departamento : String,
    @SerializedName("provincia") var provincia : String,
    @SerializedName("distrito") var distrito : String,
    @SerializedName("direccion") var direccion : String,
    @SerializedName("direccion_completa") var direccionCompleta : String,
    @SerializedName("ubigeo_reniec") var ubigeoReniec : String,
    @SerializedName("ubigeo_sunat") var ubigeoSunat : String,
    @SerializedName("ubigeo") var ubigeo : List<String>

)