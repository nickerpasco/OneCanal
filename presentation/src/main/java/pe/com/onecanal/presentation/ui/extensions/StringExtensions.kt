package pe.com.onecanal.presentation.ui.extensions

import android.annotation.SuppressLint
import android.os.Build
import android.text.Html
import android.text.Spanned
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

/**
 * Created by Sergio Carrillo Diestra on 20/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/


/**
 * Remove all white spaces at the end AND the beginning of the string.
 * After that, replace all the white spaces inside the current string for only one space.
 *
 * @sample  [String] "     some     word     " -> Result -> "some word"
 */
fun String.fixBlankSpaces(): String {
    val moreThanOneBlankSpaceRegex = Regex("[\\s]+")
    return this.trim().replace(moreThanOneBlankSpaceRegex, " ")
}

/**
 * @return [String] as Date formatted with the [desiredPattern]
 *
 * @param currentPattern the current pattern of the actual string.
 * @param desiredPattern the result pattern of the actual string.
 */
@SuppressLint("DefaultLocale")
fun String.toCustomDateFormat(currentPattern: String, desiredPattern: String): String? {
    try {
        return if (this.trim().isNotEmpty()) {
            val sdfServer = SimpleDateFormat(currentPattern, Locale.getDefault())
            val dateFromServer = sdfServer.parse(this)
            return if (dateFromServer != null) {
                val sdf = SimpleDateFormat(desiredPattern, Locale.getDefault())
                sdf.format(dateFromServer).replace(".", "")
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            } else {
                null
            }
        } else {
            null
        }
    } catch (e: Exception) {
        return null
    }
}

/**
 * Splits a sentence and Capitalize the first letter of each word, the rest of
 * the word is turned to lowercase.
 *
 * @sample "some word" -> "Some Word"
 * @sample "JOHN   WAYNE  " -> "John Wayne"
 */
fun String.capitalizeFirstLetters(): String {
    val cleanWord = this.fixBlankSpaces()
    return if (cleanWord.isNotEmpty()) {
        val words = cleanWord.split(" ")
        var sentence = ""
        words.forEach {
            sentence += " ${it[0].uppercaseChar()}${
                it.substring(1, it.length)
                    .lowercase(Locale.getDefault())
            }"
        }
        sentence.trim()
    } else {
        "-"
    }
}

/**
 * Returns [Spanned] with the given text with two colors.
 *
 * @param secondText text to concatenate.
 * @param firstColor color of the first block of text. Which is the text we are extending this function.
 * @param secondColor color for [secondText]
 * @param jumpOfLineOrWhiteSpace use <br/> to override the empty space and create a jump of line.
 */
fun String.toSpanned(
    secondText: Any?,
    firstColor: String = "#000000",
    secondColor: String = "#000000",
    jumpOfLineOrWhiteSpace: String = " "
): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(
            "<b><font color=$firstColor>$this</font></b>$jumpOfLineOrWhiteSpace<font color=$secondColor>$secondText</font>",
            Html.FROM_HTML_MODE_COMPACT
        )
    } else {
        Html.fromHtml("<b><font color=$firstColor>$this</font></b>$jumpOfLineOrWhiteSpace<font color=$secondColor>$secondText</font>")
    }
}

fun String.toDoubleWithTwoDecimals(): Double {
    return BigDecimal(this.replace(",", ".").trim()).setScale(2, RoundingMode.HALF_UP).toDouble()
}
fun String.toDoubleWithFourDecimals(): Double {
    return BigDecimal(this.replace(",", ".").trim()).setScale(4, RoundingMode.HALF_UP).toDouble()
}

fun String.isANumber(): Boolean {
    return try {
        this.toDouble()
        true
    } catch (e: Exception) {
        false
    }
}

fun String?.validateDocument(): Boolean {
    val numberRegex = Regex("\\w+?")
    return if (this != null) numberRegex.matches(this) else false
}

fun String.isEmail(): Boolean {
    val emailRegex =
        Regex("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&’*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$")
    return this.contains(".") && this.trim().length > 5 && emailRegex.matches(this.trim())
}

fun String?.isAlphaNumeric(): Boolean {
    val stringRegex = Regex("^(?=.*[0-9])(?=.*[a-z])([a-z0-9]+)\$")
    return if (this != null) stringRegex.matches(this.lowercase(Locale.getDefault())) else false
}

fun String?.isNumeric(): Boolean {
    val numberRegex = Regex("\\d+?")
    return if (this != null) numberRegex.matches(this) else false
}

fun String?.isValidSalaryAdvanceDecimalDecimal(): Boolean {
    val stringRegex = Regex("^(\\d+(?:[\\.\\,]\\d{1,2})?)\$")
    return if (this != null) stringRegex.matches(this.trim()) else false
}

fun String.existsInString(source: String): Boolean {
    val pattern = "\\b$this\\b"
    val p = Pattern.compile(pattern)
    val m = p.matcher(source)
    return m.find()
}

