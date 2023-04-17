package pe.com.onecanal.domain.entity

import java.io.Serializable

data class ValidateData(
    var userId: Int,
    val email: String,
) : Serializable