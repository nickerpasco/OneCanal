package pe.com.onecanal.presentation.features.salaryAdvance.stepOne

import app.cash.turbine.test
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import pe.com.onecanal.domain.entity.Either
import pe.com.onecanal.domain.entity.Failure
import pe.com.onecanal.domain.entity.SalaryAdvanceReason
import pe.com.onecanal.domain.repository.AppRepository
import pe.com.onecanal.domain.usecases.*
import pe.com.onecanal.presentation.ui.features.salaryAdvance.stepOne.intent.SalaryAdvanceStepOneIntent
import pe.com.onecanal.presentation.ui.features.salaryAdvance.stepOne.state.SalaryAdvanceStepOneIntentState
import pe.com.onecanal.presentation.ui.features.salaryAdvance.stepOne.state.SalaryAdvanceStepOneIntentState.*
import pe.com.onecanal.presentation.ui.features.salaryAdvance.stepOne.viewModel.SalaryAdvanceStepOneViewModel
import pe.com.onecanal.util.BaseUnitTest
import kotlin.Error

/**
 * Created by Sergio Carrillo Diestra on 22/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/

@RunWith(MockitoJUnitRunner::class)
class ViewModelShould : BaseUnitTest() {
    @Mock
    private lateinit var getSalarySwitchStateUseCase: GetSalarySwitchStateUseCase

    @Mock
    private lateinit var saveSalarySwitchStateUseCase: SaveSalarySwitchStateUseCase

    @Mock
    private lateinit var getSalaryUseCase: GetSalaryUseCase

    @Mock
    private lateinit var appRepository: AppRepository

    @Mock
    private lateinit var getFeesUseCase: GetFeesUseCase

    @Mock
    private lateinit var expected: List<SalaryAdvanceReason>
    private lateinit var getSalaryAdvanceReasonsUseCase: GetSalaryAdvanceReasonsUseCase
    private val expectedServerError = Failure.ServerBodyError(0, "server error")
    private lateinit var viewModel: SalaryAdvanceStepOneViewModel

    @Before
    fun setUp() {
        getSalaryAdvanceReasonsUseCase = GetSalaryAdvanceReasonsUseCase(appRepository)
        viewModel = SalaryAdvanceStepOneViewModel(
            getFeesUseCase,
            getSalaryAdvanceReasonsUseCase,
            getSalaryUseCase,
            getSalarySwitchStateUseCase,
            saveSalarySwitchStateUseCase
        )
    }

    @Test
    fun `return error when error received`() = runBlockingTest {
        `when`(appRepository.getSalaryAdvanceReasons()).thenReturn(
            Either.Error(expectedServerError)
        )
        viewModel.userIntent.send(SalaryAdvanceStepOneIntent.GetSalaryAdvanceReasons)
        launch {
            viewModel.intentState.test {
                assertEquals(expectedServerError, (expectMostRecentItem() as SalaryAdvanceStepOneIntentState.Error).error)
            }
        }

    }

    @Test
    fun `return salary advance reasons from repository`() = runBlockingTest {
        `when`(appRepository.getSalaryAdvanceReasons()).thenReturn(Either.Success(expected))
        viewModel.userIntent.send(SalaryAdvanceStepOneIntent.GetSalaryAdvanceReasons)
        viewModel.intentState.test {
            assertEquals(expected, (expectMostRecentItem() as SalaryAdvanceReasonFetched).reasons)
//            cancelAndIgnoreRemainingEvents()
        }
    }

}