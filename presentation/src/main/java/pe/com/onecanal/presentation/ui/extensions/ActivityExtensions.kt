package pe.com.onecanal.presentation.ui.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import java.io.Serializable

inline fun <reified T : Activity> Context.startNewActivityClearStack() {
    this.startActivity(Intent(this, T::class.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
    })
}

inline fun <reified T : Activity> Context.startNewActivity() {
    this.startActivity(Intent(this, T::class.java))
}

inline fun <reified T : Activity> Context.startNewActivity(
    vararg extras: Pair<String, Any>?,
    clearStack: Boolean = false
) {
    this.startActivity(Intent(this, T::class.java).apply {
        extras.forEach {
            it.let { pairNotNull ->
                when (val pairValue = pairNotNull?.second) {
                    is Int -> putExtra(pairNotNull.first, pairValue)
                    is Long -> this.putExtra(pairNotNull.first, pairValue)
                    is CharSequence -> this.putExtra(pairNotNull.first, pairValue)
                    is String -> this.putExtra(pairNotNull.first, pairValue)
                    is Float -> this.putExtra(pairNotNull.first, pairValue)
                    is Double -> this.putExtra(pairNotNull.first, pairValue)
                    is Char -> this.putExtra(pairNotNull.first, pairValue)
                    is Short -> this.putExtra(pairNotNull.first, pairValue)
                    is Boolean -> this.putExtra(pairNotNull.first, pairValue)
                    is Bundle -> this.putExtra(pairNotNull.first, pairValue)
                    is Parcelable -> this.putExtra(pairNotNull.first, pairValue)
                    is Array<*> -> when {
                        pairValue.isArrayOf<CharSequence>() -> this.putExtra(
                            pairNotNull.first,
                            pairValue
                        )
                        pairValue.isArrayOf<String>() -> this.putExtra(pairNotNull.first, pairValue)
                        pairValue.isArrayOf<Parcelable>() -> this.putExtra(
                            pairNotNull.first,
                            pairValue
                        )
                        else -> throw IllegalArgumentException("Intent extra ${pairNotNull.first} has wrong type ${pairValue.javaClass.name}")
                    }
                    is IntArray -> this.putExtra(pairNotNull.first, pairValue)
                    is LongArray -> this.putExtra(pairNotNull.first, pairValue)
                    is FloatArray -> this.putExtra(pairNotNull.first, pairValue)
                    is DoubleArray -> this.putExtra(pairNotNull.first, pairValue)
                    is CharArray -> this.putExtra(pairNotNull.first, pairValue)
                    is ShortArray -> this.putExtra(pairNotNull.first, pairValue)
                    is BooleanArray -> this.putExtra(pairNotNull.first, pairValue)
                    is Serializable -> this.putExtra(pairNotNull.first, pairValue)
                    else -> throw IllegalArgumentException("Intent extra ${pairNotNull?.first} has wrong type ${pairValue?.javaClass?.name}")
                }
                return@forEach
            }
        }
        if (clearStack)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
    })


}

fun Context.dp2px(dp: Int): Int {
    val scale = resources.displayMetrics.density
    return (dp.toFloat() * scale + 0.5f).toInt()
}
inline fun <reified T : Activity> Context.startActivityE(vararg extras: Serializable) {
    val intent = Intent(this, T::class.java)

    for (i in extras.indices) {
        intent.putExtra("extra$i", extras[i])
    }

    startActivity(intent)
}


