package pe.com.onecanal.domain.entity

import java.util.*

/**
 * Created by Sergio Carrillo Diestra on 13/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
data class UserProfileData(
    var userToken: String,
    val name: String,
    val lastName: String,
    val email: String,
    val documentType:String,
    var documentNumber: String,
    val business: String,
    val ruc: String,
    val salary:Double,
    val available_salary:Double,
    val accounts: List<BankAccount>,
    val maritalId: Int?,
    val maritalName: String?,
    val address: String?,
    val businessJob: String?
) {
    fun getShorFullName(): String {
        val arr = lastName.split(" ")
        val lastName = arr[0]
        return "${name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }} $lastName"
    }
}

