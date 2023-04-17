package pe.com.onecanal.presentation.features.login

import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import pe.com.onecanal.domain.usecases.*
import pe.com.onecanal.presentation.ui.features.salaryAdvance.stepOne.viewModel.SalaryAdvanceStepOneViewModel
import pe.com.onecanal.util.BaseUnitTest

/**
 * Created by Sergio Carrillo Diestra on 25/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
@RunWith(MockitoJUnitRunner::class)
class DocumentTypeFormInputShould: BaseUnitTest() {
    @Mock
    private lateinit var saveSalarySwitchStateUseCase: SaveSalarySwitchStateUseCase
    @Mock
    private lateinit var getSalarySwitchStateUseCase: GetSalarySwitchStateUseCase
    @Mock
    private lateinit var getSalaryUseCase: GetSalaryUseCase
    @Mock
    private lateinit var getSalaryAdvanceReasonsUseCase: GetSalaryAdvanceReasonsUseCase
    @Mock
    private lateinit var getFeesUseCase: GetFeesUseCase
    @Mock
    private lateinit var getBankAccountsUseCase: GetBankAccountsFromSessionUseCase
    private lateinit var viewModel: SalaryAdvanceStepOneViewModel

    @Before
    fun setUp() {
        viewModel = SalaryAdvanceStepOneViewModel(
            getFeesUseCase,
            getSalaryAdvanceReasonsUseCase,
            getSalaryUseCase,
            getSalarySwitchStateUseCase,
            saveSalarySwitchStateUseCase,
            getBankAccountsUseCase
        )
    }


    @Test
    fun `return false when SalaryAdvanceReason are invalid`() {
        configureSalaryAdvanceReasonDropDown("")
        viewModel.apply {
            TestCase.assertEquals(false, advanceReasonField.fieldIsValid())
        }
    }

    private fun configureSalaryAdvanceReasonDropDown(actualValue: String) {
        viewModel.advanceReasonField.also {
            it.fieldText.value = actualValue
        }
    }

}