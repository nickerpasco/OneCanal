package pe.com.onecanal.presentation.ui.extensions

import pe.com.onecanal.presentation.ui.config.Settings
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Created by Sergio Carrillo Diestra on 20/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
fun Double.toTwoDecimalStr(): String {
  return  "%.2f".format(this)
}

fun Double.toFourDecimalStr(): String {
  return  "%.4f".format(this)
}

fun Double.showAsCurrency():String{
  return "${Settings.CURRENCY_SYMBOL} ${this.toTwoDecimalStr()}"
}

fun Double.toDoubleWithTwoDecimals(): Double {
  return BigDecimal(this).setScale(2, RoundingMode.HALF_UP).toDouble()
}

fun Double.toDoubleWithFourDecimals(): Double {
  return BigDecimal(this).setScale(4, RoundingMode.HALF_UP).toDouble()
}

