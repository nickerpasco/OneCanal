package pe.com.onecanal.framework.hilt.utilsreniec

import com.google.gson.annotations.SerializedName
import pe.com.onecanal.domain.utilsreniec.Contenido

data class ReponseDNI(
    @SerializedName("Exito") var Exito : Boolean,
    @SerializedName("MostrarAdvertencia") var MostrarAdvertencia : Boolean,
    @SerializedName("Codigo") var Codigo : String,
    @SerializedName("Mensaje") var Mensaje : String,
    @SerializedName("MensajeExcepcion") var MensajeExcepcion : String,
    @SerializedName("Traza") var Traza : String,
    @SerializedName("Contenido") var Contenido : Contenido,
    @SerializedName("CantidadFilas") var CantidadFilas : Int,
    @SerializedName("recordsTotal") var recordsTotal : Int,
    @SerializedName("recordsFiltered") var recordsFiltered : Int

)

