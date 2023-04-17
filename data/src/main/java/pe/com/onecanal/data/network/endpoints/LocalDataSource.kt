package pe.com.onecanal.data.network.endpoints

import pe.com.onecanal.domain.entity.*

/**
 * Created by Sergio Carrillo Diestra on 19/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
interface LocalDataSource {
    suspend fun getSalary(): Either<Failure, Salary>
    suspend fun saveUserSession(userProfileData: UserProfileData): Either<Failure, Unit>
    suspend fun getUserSession(): Either<Failure, UserProfileData?>
    suspend fun getBankAccountsFromSession(): Either<Failure, List<BankAccount>>
    suspend fun getToken(): String
    fun getSalarySwitchState(): Boolean
    suspend fun saveSalarySwitchState(state: Boolean): Either<Failure, Unit>
    suspend fun clearUserSession(): Either<Failure, Any>
}