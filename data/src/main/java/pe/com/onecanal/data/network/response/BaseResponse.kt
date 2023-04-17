package pe.com.onecanal.data.network.response

data class BaseResponse<R>(
    val data: R,
    val code: Int,
    val message: String
)