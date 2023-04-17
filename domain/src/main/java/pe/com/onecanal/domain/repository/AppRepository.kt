package pe.com.onecanal.domain.repository

import pe.com.onecanal.domain.entity.*

/**
 * Created by Sergio Carrillo Diestra on 10/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
interface AppRepository {
    suspend fun getDocumentTypes(): Either<Failure, List<DocumentType>>
    suspend fun validateAccount(documentType: String, documentNumber: String): Either<Failure, ValidateData>
    suspend fun validateToken(code: String, email: String, userId: Int ): Either<Failure, Any>
    suspend fun createNewPassword(
        code: String,
        email: String,
        userId: Int,
        password: String,
        password_confirmation: String
    ): Either<Failure, Any>

    suspend fun doLogin(
        documentType: String,
        documentNumber: String,
        password: String
    ): Either<Failure, UserProfileData>

    suspend fun forgotPassword(documentType: String, documentNumer: String): Either<Failure, ValidateData>
    suspend fun getSalaryAdvanceReasons(): Either<Failure, List<SalaryAdvanceReason>>
    suspend fun getFees(): Either<Failure, FeeDomain>
    suspend fun getSalary(): Either<Failure, Salary>
    suspend fun saveUserSession(params: UserProfileData): Either<Failure, Unit>
    suspend fun getUserSession(): Either<Failure, UserProfileData?>
    suspend fun getBankAccountsFromSession(): Either<Failure, List<BankAccount>>
    suspend fun getSalaryAdvanceDetails(amount: Double): Either<Failure, SalaryAdvanceDetails>
    suspend fun saveSalaryAdvance(
        account_id: Int,
        amount: Double,
        fees: String,
        period_name: String,
        reason_id: Int,
        transfer_amount: String
    ): Either<Failure, Any>

    fun getSalarySwitchState(): Boolean
    suspend fun saveSalarySwitchState(state: Boolean): Either<Failure, Unit>
    suspend fun getSalaryAdvanceHistory(page: Int): Either<Failure, HistoryPagination>
    suspend fun changePassword(
        oldPassword: String,
        newPassword: String,
        newPasswordConfirmation: String
    ): Either<Failure, Any>

    suspend fun getUserDataFromRemote(): Either<Failure, UserProfileData>
    suspend fun clearUserSession(): Either<Failure, Any>
    suspend fun getTermsAndConditions(type: String?): Either<Failure, String>
    suspend fun getSalaryAdvanceFormat(): Either<Failure, String>

    suspend fun getBanks(): Either<Failure, List<AccountBank>>
    suspend fun saveBanksAccount(
        bankId: String,
        number: String,
        cci: String
    ): Either<Failure, Any>

    suspend fun getMaritalStatus(): Either<Failure, List<MaritalStatus>>
    suspend fun completeProfile(
        maritalStatusId: Int,
        address: String,
        businessJob: String,
        salary: String
    ): Either<Failure, UserProfileData>
}