package pe.com.onecanal.data.network.endpoints


import pe.com.onecanal.data.network.body.*
import pe.com.onecanal.data.network.response.*
import pe.com.onecanal.data.util.NetworkUtils
import pe.com.onecanal.domain.entity.Either
import pe.com.onecanal.domain.entity.Failure

class RemoteDataSourceImpl(
    private val api: RemoteAPI,
    private val networkUtils: NetworkUtils
) : RemoteDataSource {
    override suspend fun getDocumentTypes(): Either<Failure, List<DocumentTypeResponse>> {
        return networkUtils.callFromRemoteDataSource { api.getDocumentTypes() }
    }

    override suspend fun validateAccount(body: AccountValidationBody): Either<Failure, ValidateResponse> {
        return networkUtils.callFromRemoteDataSource { api.validateAccount(body) }
    }

    override suspend fun validateToken(body: ValidateTokenBody): Either<Failure, Any> {
        return networkUtils.callFromRemoteDataSource { api.validateToken(body) }
    }

    override suspend fun createNewPassword(body: ValidatePasswordBody): Either<Failure, Any> {
        return networkUtils.callFromRemoteDataSource { api.createNewPassword(body) }
    }

    override suspend fun login(body: LoginBody): Either<Failure, LoginResponse> {
        return networkUtils.callFromRemoteDataSource { api.doLogin(body) }
    }

    override suspend fun forgotPassword(forgotPasswordBody: ForgotPasswordBody): Either<Failure, ValidateResponse> {
        return networkUtils.callFromRemoteDataSource { api.forgotPassword(forgotPasswordBody) }
    }

    override suspend fun getSalaryAdvanceReasons(): Either<Failure, List<SalaryAdvanceReasonReponse>> {
        return networkUtils.callFromRemoteDataSource { api.getSalaryAdvanceReasons() }
    }

    override suspend fun getFees(): Either<Failure, List<FeeResponse>> {
        return networkUtils.callFromRemoteDataSource { api.getFees() }
    }

    override suspend fun getSalaryAdvanceDetails(amount: Double): Either<Failure, SalaryAdvanceDetailsReponse> {
        return networkUtils.callFromRemoteDataSource { api.getSalaryAdvanceDetails(amount) }
    }

    override suspend fun saveSalaryAdvance(salaryAdvanceBody: SalaryAdvanceBody): Either<Failure, Any> {
        return networkUtils.callFromRemoteDataSource { api.saveSalaryAdvance(salaryAdvanceBody) }
    }

    override suspend fun getSalaryAdvanceHistory(page: Int): Either<Failure, HistoryResponse> {
        return networkUtils.callFromRemoteDataSource { api.getSalaryAdvanceHistory(page) }
    }

    override suspend fun changePassword(
        oldPassword: String,
        newPassword: String,
        newPasswordConfirmation: String
    ): Either<Failure, Any> {
        return networkUtils.callFromRemoteDataSource { api.changePassword(ChangePasswordBody(oldPassword,newPassword,newPasswordConfirmation)) }
    }

    override suspend fun getUserData(): Either<Failure, MeResponse> {
        return networkUtils.callFromRemoteDataSource { api.getUserData() }

    }

    override suspend fun getTermsAndConditions(type: String?): Either<Failure, TermsAndConditionsResponse> {
        return networkUtils.callFromRemoteDataSource { api.getTermsAndConditions(type = type) }
    }

    override suspend fun getSalaryAdvanceFormat(): Either<Failure, TermsAndConditionsResponse> {
        return networkUtils.callFromRemoteDataSource { api.getSalaryAdvanceFormat() }
    }

    override suspend fun getBanks(): Either<Failure, List<AccountBankResponse>> {
        return networkUtils.callFromRemoteDataSource { api.getBanks() }
    }

    override suspend fun saveBanksAccount(body: BankBody): Either<Failure, Any> {
        return networkUtils.callFromRemoteDataSource { api.saveBanksAccount(body) }
    }

    override suspend fun getMaritalStatus(): Either<Failure, List<MaritalStatusResponse>> {
        return networkUtils.callFromRemoteDataSource { api.getMaritalStatus() }
    }

    override suspend fun completeProfile(body: CompleteProfileBody): Either<Failure, MeResponse> {
        return networkUtils.callFromRemoteDataSource { api.completeProfile(body) }
    }


}