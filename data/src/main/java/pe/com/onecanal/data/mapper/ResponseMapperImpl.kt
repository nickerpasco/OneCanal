package pe.com.onecanal.data.mapper

import com.google.gson.Gson
import pe.com.onecanal.data.network.response.*
import pe.com.onecanal.domain.config.Settings
import pe.com.onecanal.domain.entity.*


/**
 * Created by Sergio Carrillo Diestra on 11/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
class ResponseMapperImpl : ResponseMapper {

    override fun salaryAdvanceReasonResponseToDomain(response: List<SalaryAdvanceReasonReponse>): List<SalaryAdvanceReason> {
        return response.map {
            SalaryAdvanceReason(
                id = it.id,
                name = it.name
            )
        }
    }

    override fun loginResponseToUserProfile(response: LoginResponse): UserProfileData {
        return UserProfileData(
            response.access_token,
            response.me.names,
            response.me.surnames,
            response.me.email,
            response.me.document_type,
            response.me.document_number,
            response.me.business.name,
            response.me.business.ruc,
            response.me.salary,
            response.me.available_salary,
            response.me.accounts.map {
                BankAccount(
                    id = it.id,
                    number = it.number,
                    shortName = it.bank.short_name ?: "",
                    active = it.active,
                    confirmed = it.confirmed
                )
            },
            (response.me.marital_status ?: MaritalStatusResponse(id = null, name = null)).id,
            (response.me.marital_status ?: MaritalStatusResponse(id = null, name = null)).name,
            response.me.address,
            response.me.business_job,
        )
    }

    override fun validateResponseToValidateData(response: ValidateResponse): ValidateData {
        return ValidateData(
            email = response.email,
            userId = response.userId
        )
    }

    override fun feeResponseToDomain(successes: List<FeeResponse>): FeeDomain {
        val igv = successes.find { it.type == Settings.IGV }
        val itf = successes.find { it.type == Settings.ITF }
        val fee = successes.find { it.type == Settings.FEE }
        val asd = "{\"items\":" + Gson().toJson(fee!!.value) + "}"
        val fees = Gson().fromJson(asd, FeeHelper::class.java)
        val igvs = igv!!.value as String
        val itfs = itf!!.value as String


        return FeeDomain(
            feeRanges = FeeType(fee.id, fee.type, fees.items),
            igv = FeeType(igv.id, igv.type, igvs.toDouble()),
            itf=FeeType(itf.id, itf.type, itfs.toDouble())
        )
    }

    override fun documentTypeToDomain(success: List<DocumentTypeResponse>): List<DocumentType> =
        success.map { DocumentType(id = it.id, name = it.short_name) }

    override fun salaryAdvanceDetailsToDomain(response: SalaryAdvanceDetailsReponse): SalaryAdvanceDetails {
        return SalaryAdvanceDetails(
            amount = response.amount,
            fees = response.fees,
            period_name = response.period_name,
            transfer_amount = response.transfer_amount,
            date = response.date
        )
    }

    override fun historyResponseToDomain(success: HistoryResponse): HistoryPagination {
        val items = success.data.map {
            HistoryItem(
                account = "${it.account.bank.short_name} - ${it.account.number}",
                amount = it.amount,
                date = it.date,
                periodName = it.period_name,
                feesAmount = it.fees_amount,
                reason = it.reason.name,
                status = SalaryAdvanceStatus.valueOf(it.status),
                transferAmount = it.transfer_amount
            )
        }
        return HistoryPagination(
            items = items,
            meta = MetaData(
                lastPage = success.meta.last_page,
                currentPage = success.meta.current_page
            )
        )
    }

    override fun meToUserProfileWithEmptyToken(success: MeResponse): UserProfileData {
        return UserProfileData(
            "",
            success.names,
            success.surnames,
            success.email,
            success.document_type,
            success.document_number,
            success.business.name,
            success.business.ruc,
            success.salary,
            success.available_salary,
            success.accounts.map {
                BankAccount(
                    id = it.id,
                    number = it.number,
                    shortName = it.bank.short_name ?: "",
                    active = it.active,
                    confirmed = it.confirmed
                )
            },
            (success.marital_status ?: MaritalStatusResponse(id = null, name = null)).id,
            (success.marital_status ?: MaritalStatusResponse(id = null, name = null)).name,
            success.address ?: "",
            success.business_job ?: "",
        )
    }

    override fun accountBankToDomain(response: List<AccountBankResponse>): List<AccountBank> {
        return response.map {
            AccountBank(
                id = it.id,
                name = it.name,
                shortName = it.short_name
            )
        }
    }

    override fun maritalStatusToDomain(response: List<MaritalStatusResponse>): List<MaritalStatus> {
        return response.map {
            MaritalStatus(
                id = it.id!!,
                name = it.name!!
            )
        }
    }
}