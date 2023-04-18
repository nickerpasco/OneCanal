package pe.com.onecanal.framework.hilt.utilsreniec

import com.google.gson.Gson
import pe.com.onecanal.domain.utilsreniec.ErrorResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class RestApiService {



    fun getDataReniec(userData: reniecboby, onResult: (ReponseDNI?) -> Unit){



        try {

            val retrofit = ServiceBuilder.buildService(RestApi::class.java)
            retrofit.getdataDNI(userData).enqueue(
                object : Callback<ReponseDNI> {
                    override fun onFailure(call: Call<ReponseDNI>, t: Throwable) {
                        onResult(null)
                    }
                    override fun onResponse(call: Call<ReponseDNI>, response: Response<ReponseDNI>) {


                        if(response.code()==403){

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


    private fun isServerErrorValid(error: String): Boolean {
        return error.contains("\"$KEY_STATUS\"") || error.contains("\"$KEY_MESSAGE\"")
    }


    companion object {
        private const val KEY_STATUS = "code"
        private const val KEY_MESSAGE = "message"
    }
}