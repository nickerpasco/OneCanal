package pe.com.onecanal.framework.hilt.utilsreniec

import com.google.gson.JsonObject
import pe.com.onecanal.domain.dtos.DtoUsers
import pe.com.onecanal.domain.dtos.ResponseUsers
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


interface RestApi {


    @Headers("Content-Type: application/json")
    @POST("api/v1/auth/getapiperuapp")
    fun getdataDNI(@Body reniecboby: reniecboby): Call<ReponseDNI>


    @Headers("Content-Type: application/json")
    @POST("api/v1/auth/updateusers")
    fun updateusers(@Body body: DtoUsers): Call<ResponseUsers>
}