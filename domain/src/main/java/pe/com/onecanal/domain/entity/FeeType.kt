package pe.com.onecanal.domain.entity

data class FeeType<T>(
    val id: Int,
    val type: String,
    val value: T
)