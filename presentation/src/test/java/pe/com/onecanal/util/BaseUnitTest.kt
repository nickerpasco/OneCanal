package pe.com.onecanal.util

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Rule

open class BaseUnitTest {
    @get:Rule
    var coroutinesTestRule = MainCoroutineScopeRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
}