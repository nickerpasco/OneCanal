package pe.com.onecanal.presentation.ui.extensions

import android.view.View
import android.widget.TextView
import pe.com.onecanal.presentation.ui.util.SafeDoubleClick

fun View.setFasterClickListener(onDebounceClick: (View) -> Unit) {
    val safeClickListener = SafeDoubleClick {
        onDebounceClick(it)
    }
    setOnClickListener(safeClickListener)
}

fun TextView.getString() : String {
    return this.text.trim().toString()
}