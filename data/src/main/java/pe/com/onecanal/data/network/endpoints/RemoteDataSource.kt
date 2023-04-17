package pe.com.onecanal.data.network.endpoints

import pe.com.onecanal.data.network.body.*
import pe.com.onecanal.data.network.response.*
import pe.com.onecanal.domain.entity.Either
import pe.com.onecanal.domain.entity.Failure


interface RemoteDataSource {
    suspend fun getDocumentTypes(): Either<Failure, List<DocumentTypeResponse>>
    suspend fun validateAccount(body: AccountValidationBody): Either<Failure, ValidateResponse>
    suspend fun validateToken(body: ValidateTokenBody): Either<Failure, Any>
    suspend fun createNewPassword(body: ValidatePasswordBody): Either<Failure, Any>
    suspend fun login(body: LoginBody): Either<Failure, LoginResponse>
    suspend fun forgotPassword(forgotPasswordBody: ForgotPasswordBody): Either<Failure, ValidateResponse>
    suspend fun getSalaryAdvanceReasons(): Either<Failure, List<SalaryAdvanceReasonReponse>>
    suspend fun getFees(): Either<Failure, List<FeeResponse>>
    suspend fun getSalaryAdvanceDetails(amount: Double): Either<Failure, SalaryAdvanceDetailsReponse>
    suspend fun saveSalaryAdvance(salaryAdvanceBody: SalaryAdvanceBody): Either<Failure, Any>
    suspend fun getSalaryAdvanceHistory(page: Int): Either<Failure, HistoryResponse>
    suspend fun changePassword(
        oldPassword: String,
        newPassword: String,
        newPasswordConfirmation: String
    ): Either<Failure, Any>

    suspend fun getUserData(): Either<Failure, MeResponse>
    suspend fun getTermsAndConditions(type: String?): Either<Failure,TermsAndConditionsResponse>
    suspend fun getSalaryAdvanceFormat(): Either<Failure,TermsAndConditionsResponse>
    suspend fun getBanks(): Either<Failure, List<AccountBankResponse>>
    suspend fun saveBanksAccount(body: BankBody): Either<Failure, Any>
    suspend fun getMaritalStatus(): Either<Failure, List<MaritalStatusResponse>>
    suspend fun completeProfile(body: CompleteProfileBody): Either<Failure, MeResponse>
}