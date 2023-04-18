package pe.com.onecanal.framework.hilt.utilsreniec

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


//import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {
    private val client = OkHttpClient.Builder().build()

    val httpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request()
            val response = chain.proceed(request)
            if (response.code == 403) {
                val responseBody = response.body?.string()
                // Aquí puedes hacer lo que necesites con el cuerpo de la respuesta
                // por ejemplo, imprimirlo en el LogCat o analizarlo como JSON

            }
            response
        }
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://onecanal.rsdev.site/onecanal/") // change this IP for testing by your actual machine IP
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient)
        .client(client)
        .build()

    fun<T> buildService(service: Class<T>): T{

        return retrofit.create(service)
    }
}