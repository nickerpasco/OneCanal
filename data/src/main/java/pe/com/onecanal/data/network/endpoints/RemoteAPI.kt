package pe.com.onecanal.data.network.endpoints

import pe.com.onecanal.data.network.body.*
import pe.com.onecanal.data.network.response.*
import retrofit2.Response
import retrofit2.http.*

interface RemoteAPI {

    //AUTH REGION
    @POST("v1/auth/login")
    suspend fun doLogin(@Body loginBody: LoginBody): Response<BaseResponse<LoginResponse>>

    @POST("v2/auth/validate")
    suspend fun validateAccount(@Body accountValidationBody: AccountValidationBody): Response<BaseResponse<ValidateResponse>>

    @POST("v2/auth/validate/token")
    suspend fun validateToken(@Body validatePasswordBody: ValidateTokenBody): Response<BaseResponse<Any>>

    @POST("v2/auth/validate/password")
    suspend fun createNewPassword(@Body validatePasswordBody: ValidatePasswordBody): Response<BaseResponse<Any>>

    @POST("v2/auth/forgot-password")
    suspend fun forgotPassword(@Body forgotPasswordBody: ForgotPasswordBody): Response<BaseResponse<ValidateResponse>>


    //MASTER REGION

    @GET("v1/masters/document_types")
    suspend fun getDocumentTypes(): Response<BaseResponse<List<DocumentTypeResponse>>>

    @GET("v1/masters/reasons")
    suspend fun getSalaryAdvanceReasons(): Response<BaseResponse<List<SalaryAdvanceReasonReponse>>>

    @GET("v1/masters/fees")
    suspend fun getFees(): Response<BaseResponse<List<FeeResponse>>>

    @GET("v1/masters/terms")
    suspend fun getTermsAndConditions(
        @Query("type") type: String?
    ): Response<BaseResponse<TermsAndConditionsResponse>>

    @GET("v1/masters/salary-advance-format")
    suspend fun getSalaryAdvanceFormat(): Response<BaseResponse<TermsAndConditionsResponse>>
    //SALARY REGION

    @GET("v1/salary-advances/payment-period")
    suspend fun getSalaryAdvanceDetails(@Query("amount") amount: Double): Response<BaseResponse<SalaryAdvanceDetailsReponse>>

    @POST("v1/salary-advances")
    suspend fun saveSalaryAdvance(@Body body: SalaryAdvanceBody): Response<BaseResponse<Any>>

    @GET("v1/salary-advances/history")
    suspend fun getSalaryAdvanceHistory(@Query("page") page: Int): Response<BaseResponse<HistoryResponse>>

    @PATCH("v1/me/password")
    suspend fun changePassword(@Body body: ChangePasswordBody): Response<BaseResponse<Any>>

    @GET("v1/me")
    suspend fun getUserData(): Response<BaseResponse<MeResponse>>

    //EDIT PROFILE
    @GET("v1/masters/banks")
    suspend fun getBanks(): Response<BaseResponse<List<AccountBankResponse>>>

    @POST("v1/me/accounts")
    suspend fun saveBanksAccount(@Body body: BankBody): Response<BaseResponse<Any>>

    @GET("v1/masters/marital-status")
    suspend fun getMaritalStatus(): Response<BaseResponse<List<MaritalStatusResponse>>>

    @POST("v1/me")
    suspend fun completeProfile(@Body body: CompleteProfileBody): Response<BaseResponse<MeResponse>>

}
