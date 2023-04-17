package pe.com.onecanal.extensions

import junit.framework.TestCase
import org.junit.Test
import pe.com.onecanal.presentation.ui.extensions.toDoubleWithTwoDecimals
import pe.com.onecanal.util.BaseUnitTest

/**
 * Created by Sergio Carrillo Diestra on 22/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
class ExtensionsTest: BaseUnitTest() {

    @Test
    fun `parse string salary to double and round up to two decimals`() {
        val expectedSalary: Double = 1000.52
        val salaryInput = "1000.515125125215"
        val parsedSalary = salaryInput.toDoubleWithTwoDecimals()
        TestCase.assertEquals(expectedSalary, parsedSalary)
    }
}