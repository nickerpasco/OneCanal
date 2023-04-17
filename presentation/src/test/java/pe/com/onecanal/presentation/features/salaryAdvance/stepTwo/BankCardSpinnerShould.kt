package pe.com.onecanal.presentation.features.salaryAdvance.stepTwo

import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import pe.com.onecanal.domain.usecases.GetBankAccountsFromSessionUseCase
import pe.com.onecanal.presentation.ui.features.salaryAdvance.stepTwo.viewModel.SalaryAdvanceStepTwoViewModel
import pe.com.onecanal.util.BaseUnitTest

/**
 * Created by Sergio Carrillo Diestra on 22/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/

@Suppress("SameParameterValue")
@RunWith(MockitoJUnitRunner::class)
class BankCardSpinnerShould : BaseUnitTest() {

    @Mock
    private lateinit var getBankAccountsUseCase: GetBankAccountsFromSessionUseCase

    private lateinit var viewModel:SalaryAdvanceStepTwoViewModel

    @Before
    fun setUp() {
        viewModel =SalaryAdvanceStepTwoViewModel(getBankAccountsUseCase)
    }

    @Test
    fun `when don't have errors the errorTextResource should be null`() {
        viewModel.accountField.apply {
            configureBankAccountsDropDown("some bank account")
            fieldIsValid()
            assertEquals(null, errorTextResource.value)
        }
    }

    @Test
    fun `return false when SalaryAdvanceReason are invalid`() {
        configureBankAccountsDropDown("")
        viewModel.apply {
            assertEquals(false, accountField.fieldIsValid())
        }
    }

    private fun configureBankAccountsDropDown(actualValue: String) {
        viewModel.accountField.also {
            it.fieldText.value = actualValue
        }
    }

}