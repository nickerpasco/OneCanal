package pe.com.onecanal.framework.hilt

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import pe.com.onecanal.data.network.endpoints.LocalDataSource
import pe.com.onecanal.domain.repository.AppRepository
import javax.inject.Inject

/**
 * Created by Sergio Carrillo Diestra on 23/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
class RequestInterceptor @Inject constructor(private val localDataSource: LocalDataSource) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { localDataSource.getToken() }
        val newRequest: Request = if (token.isEmpty()) {
            chain.request().newBuilder().build()
        } else {
            val auth = "Bearer $token"
            chain.request().newBuilder()
                .header("Authorization", auth)
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("Accept", "application/json")
                .build()
        }
        return chain.proceed(newRequest)
    }
}