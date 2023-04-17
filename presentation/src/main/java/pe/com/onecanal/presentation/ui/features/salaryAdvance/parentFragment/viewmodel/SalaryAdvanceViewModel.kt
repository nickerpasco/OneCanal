package pe.com.onecanal.presentation.ui.features.salaryAdvance.parentFragment.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pe.com.onecanal.domain.entity.BankAccount
import pe.com.onecanal.domain.entity.SalaryAdvanceDetails
import pe.com.onecanal.domain.entity.SalaryAdvanceReason

/**
 * Created by Sergio Carrillo Diestra on 9/01/2022.
 * Huacho, Peru.
 * scarrillo.peruapps@gmail.com
 * For Peru Apps
 *
 **/

class SalaryAdvanceViewModel : ViewModel() {
    val stepViewNumber = MutableLiveData(1)
    val showStepperView = MutableLiveData(true)

    var amount: Double = 0.0
    var account:BankAccount?=null
    var salaryAdvanceDetails: SalaryAdvanceDetails? = null
    var reason: SalaryAdvanceReason? = null


    fun dataForRequestIsValid(): Boolean {
        return try {
            return account != null &&
                    reason != null &&
                    salaryAdvanceDetails != null &&
                    salaryAdvanceDetails!!.fees.isNotEmpty() &&
                    salaryAdvanceDetails!!.transfer_amount.isNotEmpty() &&
                    salaryAdvanceDetails!!.period_name.isNotEmpty() &&
                    amount != 0.0 &&
                    reason!!.id != 0 &&
                    account!!.id != 0
        } catch (e: Exception) {
            false
        }
    }
}