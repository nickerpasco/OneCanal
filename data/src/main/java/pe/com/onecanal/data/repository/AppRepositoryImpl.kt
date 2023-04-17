package pe.com.onecanal.data.repository

import pe.com.onecanal.data.mapper.ResponseMapper
import pe.com.onecanal.data.network.body.*
import pe.com.onecanal.data.network.endpoints.LocalDataSource
import pe.com.onecanal.data.network.endpoints.RemoteDataSource
import pe.com.onecanal.data.network.body.BankBody
import pe.com.onecanal.data.network.response.MeResponse
import pe.com.onecanal.domain.entity.*
import pe.com.onecanal.domain.repository.AppRepository

/**
 * Created by Sergio Carrillo Diestra on 10/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
class AppRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val responseMapper: ResponseMapper
) : AppRepository {
    override suspend fun getDocumentTypes(): Either<Failure, List<DocumentType>> {
        return when (val response = remoteDataSource.getDocumentTypes()) {
            is Either.Success -> Either.Success(responseMapper.documentTypeToDomain(response.success))
            is Either.Error -> Either.Error(response.error)
        }
    }

    override suspend fun validateAccount(
        documentType: String,
        documentNumber: String
    ): Either<Failure, ValidateData> {
        return when (val response =
            remoteDataSource.validateAccount(AccountValidationBody(documentType, documentNumber))) {
            is Either.Success -> Either.Success(responseMapper.validateResponseToValidateData(response.success))
            is Either.Error -> Either.Error(response.error)
        }
    }

    override suspend fun validateToken(
        code: String,
        email: String,
        userId: Int
    ): Either<Failure, Any> {
        return when (val response =
            remoteDataSource.validateToken(ValidateTokenBody(code, email, userId))) {
            is Either.Success -> Either.Success(response.success)
            is Either.Error -> Either.Error(response.error)
        }
    }

    override suspend fun createNewPassword(
        code: String,
        email: String,
        userId: Int,
        password: String,
        password_confirmation: String
    ): Either<Failure, Any> {
        return when (val response = remoteDataSource.createNewPassword(
            ValidatePasswordBody(
                code,
                email,
                userId,
                password,
                password_confirmation
            )
        )) {
            is Either.Error -> Either.Error(response.error)
            is Either.Success -> Either.Success(response.success)
        }
    }

    override suspend fun doLogin(
        documentType: String,
        documentNumber: String,
        password: String
    ): Either<Failure, UserProfileData> {
        return when (val response =
            remoteDataSource.login(LoginBody(documentNumber, documentType, password))) {
            is Either.Error -> Either.Error(response.error)
            is Either.Success -> Either.Success(responseMapper.loginResponseToUserProfile(response.success))
        }
    }

    override suspend fun forgotPassword(
        documentType: String,
        documentNumer: String
    ): Either<Failure, ValidateData> {
        return when (val response =
            remoteDataSource.forgotPassword(ForgotPasswordBody(documentNumer, documentType))) {
            is Either.Error -> Either.Error(response.error)
            is Either.Success -> Either.Success(responseMapper.validateResponseToValidateData(response.success))
        }
    }

    override suspend fun getSalaryAdvanceReasons(): Either<Failure, List<SalaryAdvanceReason>> {
        return when (val response = remoteDataSource.getSalaryAdvanceReasons()) {
            is Either.Error -> Either.Error(response.error)
            is Either.Success -> Either.Success(
                responseMapper.salaryAdvanceReasonResponseToDomain(
                    response.success
                )
            )
        }
    }

    override suspend fun getFees(): Either<Failure, FeeDomain> {
        return when (val response = remoteDataSource.getFees()) {
            is Either.Error -> Either.Error(response.error)
            is Either.Success -> Either.Success(responseMapper.feeResponseToDomain(response.success))
        }
    }


    override suspend fun getSalaryAdvanceDetails(amount: Double): Either<Failure, SalaryAdvanceDetails> {
        return when (val response = remoteDataSource.getSalaryAdvanceDetails(amount)) {
            is Either.Error -> Either.Error(response.error)
            is Either.Success -> Either.Success(responseMapper.salaryAdvanceDetailsToDomain(response.success))
        }
    }

    override suspend fun saveSalaryAdvance(
        account_id: Int,
        amount: Double,
        fees: String,
        period_name: String,
        reason_id: Int,
        transfer_amount: String
    ): Either<Failure, Any> {
        return when (val response = remoteDataSource.saveSalaryAdvance(
            SalaryAdvanceBody(
                account_id = account_id,
                amount = amount,
                fees = fees,
                period_name = period_name,
                reason_id = reason_id,
                transfer_amount = transfer_amount
            )
        )) {
            is Either.Error -> Either.Error(response.error)
            is Either.Success -> Either.Success(response.success)
        }
    }

    override fun getSalarySwitchState(): Boolean {
        return localDataSource.getSalarySwitchState()
    }

    override suspend fun saveSalarySwitchState(state: Boolean): Either<Failure, Unit> {
        return localDataSource.saveSalarySwitchState(state)
    }

    override suspend fun getSalaryAdvanceHistory(page: Int): Either<Failure, HistoryPagination> {
        return when (val response = remoteDataSource.getSalaryAdvanceHistory(page)) {
            is Either.Error -> Either.Error(response.error)
            is Either.Success -> Either.Success(responseMapper.historyResponseToDomain(response.success))
        }
    }

    override suspend fun changePassword(
        oldPassword: String,
        newPassword: String,
        newPasswordConfirmation: String
    ): Either<Failure, Any> {
        return when (val response =
            remoteDataSource.changePassword(oldPassword, newPassword, newPasswordConfirmation)) {
            is Either.Error -> Either.Error(response.error)
            is Either.Success -> Either.Success(response.success)
        }
    }

    override suspend fun getUserDataFromRemote(): Either<Failure, UserProfileData> {
        return when (val response =
            remoteDataSource.getUserData()) {
            is Either.Error -> Either.Error(response.error)
            is Either.Success -> Either.Success(
                responseMapper.meToUserProfileWithEmptyToken(
                    response.success
                )
            )
        }
    }

    override suspend fun clearUserSession(): Either<Failure, Any> {
        return localDataSource.clearUserSession()
    }

    override suspend fun getTermsAndConditions(type: String?): Either<Failure, String> {
        return when (val response =
            remoteDataSource.getTermsAndConditions(type = type)) {
            is Either.Error -> Either.Error(response.error)
            is Either.Success -> Either.Success(response.success.text)
        }
    }

    override suspend fun getSalaryAdvanceFormat(): Either<Failure, String> {
        return when (val response =
            remoteDataSource.getSalaryAdvanceFormat()) {
            is Either.Error -> Either.Error(response.error)
            is Either.Success -> Either.Success(response.success.text)
        }
    }

    override suspend fun getSalary(): Either<Failure, Salary> {
        return localDataSource.getSalary()
    }

    override suspend fun saveUserSession(params: UserProfileData): Either<Failure, Unit> {
        return localDataSource.saveUserSession(params)
    }

    override suspend fun getUserSession(): Either<Failure, UserProfileData?> {
        return localDataSource.getUserSession()
    }

    override suspend fun getBankAccountsFromSession(): Either<Failure, List<BankAccount>> {
        return localDataSource.getBankAccountsFromSession()
    }

    override suspend fun getBanks(): Either<Failure, List<AccountBank>> {
        return when (val response = remoteDataSource.getBanks()) {
            is Either.Success -> Either.Success(responseMapper.accountBankToDomain(response.success))
            is Either.Error -> Either.Error(response.error)
        }
    }

    override suspend fun saveBanksAccount(
        bankId: String,
        number: String,
        cci: String
    ): Either<Failure, Any> {
        return when (val response =
            remoteDataSource.saveBanksAccount(BankBody(bankId, number, cci))) {
            is Either.Error -> Either.Error(response.error)
            is Either.Success -> Either.Success(response.success)
        }
    }

    override suspend fun getMaritalStatus(): Either<Failure, List<MaritalStatus>> {
        return when (val response = remoteDataSource.getMaritalStatus()) {
            is Either.Success -> {
                Either.Success(responseMapper.maritalStatusToDomain(response.success))
            }
            is Either.Error -> Either.Error(response.error)
        }
    }

    override suspend fun completeProfile(
        maritalStatusId: Int,
        address: String,
        businessJob: String,
        salary: String
    ): Either<Failure, UserProfileData> {
        return when (val response =
            remoteDataSource.completeProfile(CompleteProfileBody(
                maritalStatusId,
                address,
                businessJob,
                salary
            ))) {
            is Either.Error -> Either.Error(response.error)
            is Either.Success -> Either.Success(
                responseMapper.meToUserProfileWithEmptyToken(
                    response.success
                )
            )
        }
    }

}