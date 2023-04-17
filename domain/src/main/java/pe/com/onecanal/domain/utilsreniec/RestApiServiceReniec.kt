package pe.com.onecanal.framework.hilt.utilsreniec

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestApiService {



    fun getDataReniec(userData: reniecboby, onResult: (JsonObject?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.getdataDNI(userData).enqueue(
            object : Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    onResult(null)
                }
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    val addedUser = response.body()
                    onResult(addedUser)
                }
            }
        )
    }
}