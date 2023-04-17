package pe.com.onecanal.data.mapper

import pe.com.onecanal.data.network.response.*
import pe.com.onecanal.domain.entity.*

/**
 * Created by Sergio Carrillo Diestra on 11/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
interface ResponseMapper {
    fun salaryAdvanceReasonResponseToDomain(response: List<SalaryAdvanceReasonReponse>): List<SalaryAdvanceReason>
    fun loginResponseToUserProfile(response: LoginResponse): UserProfileData
    fun validateResponseToValidateData(response: ValidateResponse): ValidateData
    fun feeResponseToDomain(successes: List<FeeResponse>): FeeDomain
    fun documentTypeToDomain(success: List<DocumentTypeResponse>): List<DocumentType>
    fun salaryAdvanceDetailsToDomain(success: SalaryAdvanceDetailsReponse): SalaryAdvanceDetails
    fun historyResponseToDomain(success: HistoryResponse): HistoryPagination
    fun meToUserProfileWithEmptyToken(success: MeResponse): UserProfileData
    fun accountBankToDomain(success: List<AccountBankResponse>): List<AccountBank>
    fun maritalStatusToDomain(response: List<MaritalStatusResponse>): List<MaritalStatus>
}