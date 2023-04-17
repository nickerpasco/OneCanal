package pe.com.onecanal.presentation.features.login

import app.cash.turbine.test
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import pe.com.onecanal.domain.entity.Either
import pe.com.onecanal.domain.entity.Failure
import pe.com.onecanal.domain.entity.UserProfileData
import pe.com.onecanal.domain.repository.AppRepository
import pe.com.onecanal.domain.usecases.DoLoginUseCase
import pe.com.onecanal.domain.usecases.GetDocumentTypesUseCase
import pe.com.onecanal.domain.usecases.SaveUserSessionUseCase
import pe.com.onecanal.presentation.ui.features.login.intent.LoginIntent
import pe.com.onecanal.presentation.ui.features.login.state.LoginIntentState
import pe.com.onecanal.presentation.ui.features.login.viewmodel.LoginViewModel
import pe.com.onecanal.util.BaseUnitTest

/**
 * Created by Sergio Carrillo Diestra on 25/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ViewModelShould : BaseUnitTest() {

    private val expectedSaveUserSession: Unit = mock(Unit::class.java)
    private val saveUserSessionState = LoginIntentState.SaveUserSession

    private val userProfileData = mock(UserProfileData::class.java)
    private val expectedError = mock(Failure.DataToDomainMapperFailure::class.java)
    private val expected = mock(UserProfileData::class.java)
    private lateinit var saveUserSessionUseCase: SaveUserSessionUseCase
    private lateinit var doLoginUseCase: DoLoginUseCase
    private val getDocumentTypesUseCase = mock(GetDocumentTypesUseCase::class.java)

    private val appRepository = mock(AppRepository::class.java)
    private lateinit var viewModel: LoginViewModel
    private val documentType = "DNI"
    private val documentNumber = "48271836"
    private val password = "123456"


    @Before()
    fun setUp() {
        doLoginUseCase = DoLoginUseCase(appRepository)
        saveUserSessionUseCase = SaveUserSessionUseCase(appRepository)
        viewModel =
            LoginViewModel(getDocumentTypesUseCase, doLoginUseCase, saveUserSessionUseCase)
    }

    @Test
    fun `repository should return user profile data when DoLogin intent is send`() =
        runBlockingTest {
            `when`(appRepository.doLogin(documentType, documentNumber, password))
                .thenReturn(Either.Success(expected))
            viewModel.userIntent.send(LoginIntent.DoLogin(documentType, documentNumber, password))
            Thread.sleep(200)
            viewModel.intentState.test {
                assertEquals(
                    expected,
                    (expectMostRecentItem() as LoginIntentState.DoLogin).userProfileData
                )
            }
        }

    @Test
    fun `should return SaveUserSessionState when  saveUserSession is executed`() =
        runBlockingTest {
            `when`(appRepository.saveUserSession(userProfileData)).thenReturn(
                Either.Success(expectedSaveUserSession)
            )
            viewModel.userIntent.send(LoginIntent.SaveUserSession(userProfileData))
            Thread.sleep(200)
            viewModel.intentState.test {
                assertEquals(
                    saveUserSessionState,
                    expectMostRecentItem() as LoginIntentState.SaveUserSession
                )
            }
        }

    @Test
    fun `repository should return error when an error occurs`() =
        runBlockingTest {
            `when`(appRepository.doLogin(documentType, documentNumber, password))
                .thenReturn(Either.Error(expectedError))
            viewModel.userIntent.send(LoginIntent.DoLogin(documentType, documentNumber, password))
            Thread.sleep(200)
            viewModel.intentState.test {
                assertEquals(
                    expectedError,
                    (expectMostRecentItem() as LoginIntentState.Error).error
                )
            }
        }
}
