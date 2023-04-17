package pe.com.onecanal.framework.hilt.utilsreniec

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


interface RestApi {


    @Headers("Content-Type: application/json")
    @POST("Parametro/BuscarDNI")
    fun getdataDNI(@Body reniecboby: reniecboby): Call<JsonObject>
}