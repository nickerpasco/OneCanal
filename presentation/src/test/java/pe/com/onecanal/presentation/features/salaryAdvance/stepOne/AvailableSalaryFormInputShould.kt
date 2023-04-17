package pe.com.onecanal.presentation.features.salaryAdvance.stepOne

import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import pe.com.onecanal.R
import pe.com.onecanal.domain.usecases.*
import pe.com.onecanal.presentation.ui.extensions.toDoubleWithTwoDecimals
import pe.com.onecanal.presentation.ui.features.salaryAdvance.stepOne.viewModel.SalaryAdvanceStepOneViewModel
import pe.com.onecanal.util.BaseUnitTest

/**
 * Created by Sergio Carrillo Diestra on 22/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/

@RunWith(MockitoJUnitRunner::class)
class AvailableSalaryFormInputShould : BaseUnitTest() {

    @Mock
    private lateinit var saveSalarySwitchStateUseCase: SaveSalarySwitchStateUseCase
    @Mock
    private lateinit var getSalarySwitchStateUseCase: GetSalarySwitchStateUseCase
    @Mock
    private lateinit var getSalaryUseCase:GetSalaryUseCase
    @Mock
    private lateinit var getSalaryAdvanceReasonsUseCase:GetSalaryAdvanceReasonsUseCase
    @Mock
    private lateinit var getFeesUseCase:GetFeesUseCase
    @Mock
    private lateinit var getUserSessionUseCase:GetUserSessionUseCase
    @Mock
    private lateinit var getUserDataFromRemoteUseCase:GetUserDataFromRemoteUseCase
    @Mock
    private lateinit var saveUserSessionUseCase:SaveUserSessionUseCase
    private lateinit var viewModel: SalaryAdvanceStepOneViewModel

    @Before
    fun setUp() {
        viewModel = SalaryAdvanceStepOneViewModel(
            getFeesUseCase,
            getSalaryAdvanceReasonsUseCase,
            getSalaryUseCase,
            getSalarySwitchStateUseCase,
            saveSalarySwitchStateUseCase,
            getUserSessionUseCase,
            getUserDataFromRemoteUseCase,
            saveUserSessionUseCase
        )
    }

    @Test
    fun `show inputExceedsTheMaximumAvailable error when actual value exceed the maxValue`() {
        configureAvailableSalaryAdvanceInput(
            maxAvailableSalaryAdvance = "603.23", actualInputValue = "605"
        )
        viewModel.availableSalaryAmountField.validateMaxValue()
        assertEquals(
            viewModel.availableSalaryAmountField.errorTextResource.value,
            R.string.inputExceedsTheMaximunAvailable
        )
    }

    @Test
    fun `when don't have errors the errorTextResource should be null`() {
        viewModel.availableSalaryAmountField.apply {
            configureAvailableSalaryAdvanceInput(
                maxAvailableSalaryAdvance = "603.23", actualInputValue = "600"
            )
            fieldIsValid()
            assertEquals(null, errorTextResource.value)
        }
    }

    @Test
    fun `when the input have a comma don't throw parse error`() {
        configureAvailableSalaryAdvanceInput("500.20", "500,20")
        viewModel.availableSalaryAmountField.apply {
            assertEquals(true, fieldIsValid())
        }
    }



    @Test
    fun `return false when AvailableSalaryAdvance is not a number`() {
        configureAvailableSalaryAdvanceInput("500.20", "-asd")
        viewModel.apply {
            assertEquals(false, availableSalaryAmountField.fieldIsValid())
        }
    }

    @Test
    fun `return false when AvailableSalaryAdvance is empty`() {
        configureAvailableSalaryAdvanceInput("500.20", "")
        viewModel.apply {
            assertEquals(false, availableSalaryAmountField.fieldIsValid())
        }
    }

    private fun configureAvailableSalaryAdvanceInput(
        maxAvailableSalaryAdvance: String,
        actualInputValue: String
    ) {
        viewModel.availableSalaryAmountField.also {
            it.setMaxValue(maxAvailableSalaryAdvance.toDoubleWithTwoDecimals())
            it.fieldText.value = actualInputValue
        }
    }



}