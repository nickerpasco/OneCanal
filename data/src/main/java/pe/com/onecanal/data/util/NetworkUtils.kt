package pe.com.onecanal.data.util

import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pe.com.onecanal.data.network.response.BaseResponse
import pe.com.onecanal.domain.entity.Either
import pe.com.onecanal.domain.entity.Either.*
import pe.com.onecanal.domain.entity.Failure
import retrofit2.Response
import java.net.SocketTimeoutException
import javax.net.ssl.SSLException
import javax.net.ssl.SSLHandshakeException

class NetworkUtils(
    val connectionUtils: ConnectionUtils
) {

    /**
     * Invoke the retrofit endpoint service in IO Context and after the response has been invoked
     * verify if its successful and if has a valid body.
     */
    suspend inline fun <T> callFromRemoteDataSource(crossinline retrofitCall: suspend () -> Response<BaseResponse<T>>): Either<Failure, T> {
        return when (connectionUtils.isNetworkAvailable()) {
            true -> {
                return try {
                    withContext(Dispatchers.IO) {
                        val response = retrofitCall.invoke()
                        if (response.code() == 206) {
                            return@withContext Error(
                                Failure.ServerBodyError(
                                    response.code(), "Su cuenta no está activada"
                                )
                            )
                        } else if (response.isSuccessful && response.body() != null) {
                            return@withContext Success(response.body()!!.data)
                        } else {
                            return@withContext Error(
                                getErrorMessageFromServer(
                                    response.errorBody()?.string(), response.code()
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    return Error(parseException(e))
                }
            }
            false -> Error(Failure.NoNetworkDetected(null))
        }
    }

    /**
     * Invoke the local async task in IO Context and after the response has been invoked
     * verify if its successful and if has a not null response.
     */

    suspend inline fun <T> callFromLocalDataSource(crossinline suspendcall: suspend () -> T): Either<Failure, T> {
        return try {
            withContext(Dispatchers.IO) {
                return@withContext Success(suspendcall.invoke())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return Error(Failure.ReadFromLocalDatasourceError)
        }
    }

    /**
     * Parse Server Error to [Failure.ServerBodyError] if [errorBody] [isServerErrorValid].
     * @param errorBody the "possible" error body that the service can return.
     * @param code the HTTP status code.
     *
     * @return [Failure] object.
     */
    suspend fun getErrorMessageFromServer(errorBody: String?, code: Int): Failure {
        return if (errorBody != null) {
            return withContext(Dispatchers.IO) {
                val serverErrorJson = Gson().fromJson(errorBody, JsonObject::class.java)
                when {
                    isServerErrorValid(serverErrorJson.toString()) -> {
                        if (code == 401 || code == 403) {
                            return@withContext Failure.UnauthorizedOrForbidden(
                                serverErrorJson.get("message").asString, code
                            )
                        } else {
                            return@withContext Failure.ServerBodyError(
                                code, serverErrorJson[KEY_MESSAGE].asString
                            )
                        }
                    }
                    (code == 401 || code == 403) -> return@withContext Failure.UnauthorizedOrForbidden(
                        "error desconocido", code
                    )
                    else -> return@withContext Failure.None(code)
                }
            }
        } else {
            //No error body was found.
            Failure.None(code)
        }
    }

    private fun isServerErrorValid(error: String): Boolean {
        return error.contains("\"$KEY_STATUS\"") || error.contains("\"$KEY_MESSAGE\"")
    }

    fun parseException(throwable: Throwable): Failure {
        return when (throwable) {
            is SocketTimeoutException -> Failure.TimeOut(null)
            is SSLException -> Failure.NetworkConnectionLostSuddenly(null)
            is SSLHandshakeException -> Failure.SSLError(null)
            else -> Failure.ServiceUncaughtFailure(
                throwable.message ?: "Service response doesn't match with response object.", (null)
            )
        }
    }

    companion object {
        private const val KEY_STATUS = "code"
        private const val KEY_MESSAGE = "message"
    }
}

