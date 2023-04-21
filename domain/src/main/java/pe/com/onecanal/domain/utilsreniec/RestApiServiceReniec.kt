package pe.com.onecanal.framework.hilt.utilsreniec

import com.google.gson.Gson
import pe.com.onecanal.domain.dtos.DtoUsers
import pe.com.onecanal.domain.dtos.ResponseUsers
import pe.com.onecanal.domain.utilsreniec.ErrorResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.http.Body
import java.io.IOException

class RestApiService  {



    fun getDataReniec(userData: reniecboby, onResult: (ReponseDNI?) -> Unit){


        try {

            val retrofit = ServiceBuilder.buildService(RestApi::class.java)
            retrofit.getdataDNI(userData).enqueue(
                object : Callback<ReponseDNI> {
                    override fun onFailure(call: Call<ReponseDNI>, t: Throwable) {
                        onResult(null)
                    }
                    override fun onResponse(call: Call<ReponseDNI>, response: Response<ReponseDNI>) {


                        if(response.code()!=200){

                            try {
                                val errorBody = response.errorBody()!!.string()

                                val gson = Gson()
                                val objeto = gson.fromJson(errorBody, ReponseDNI::class.java)

                                onResult(objeto)


                            } catch (e: IOException) {
                                e.printStackTrace()
                            }

                        }else{
                            val addedUser = response.body()
                            onResult(addedUser)
                        }



                    }
                }
            )


        } catch (e: HttpException) {
            // Manejar la excepción
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            // Hacer algo con el objeto errorResponse
        }

    }

    fun updateusers(body: DtoUsers, onResult: (ResponseUsers?) -> Unit){


        try {

            val retrofit = ServiceBuilder.buildService(RestApi::class.java)
            retrofit.updateusers(body).enqueue(
                object : Callback<ResponseUsers> {
                    override fun onFailure(call: Call<ResponseUsers>, t: Throwable) {
                        onResult(null)
                    }
                    override fun onResponse(call: Call<ResponseUsers>, response: Response<ResponseUsers>) {

                        if(response.code()!=200){

                            try {
                                val errorBody = response.errorBody()!!.string()
                                val gson = Gson()
                                val objeto = gson.fromJson(errorBody, ResponseUsers::class.java)
                                onResult(objeto)
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                        }else{
                            val addedUser = response.body()
                            onResult(addedUser)
                        }



                    }
                }
            )


        } catch (e: HttpException) {
            // Manejar la excepción
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            // Hacer algo con el objeto errorResponse
        }

    }



}