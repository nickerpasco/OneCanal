package pe.com.onecanal.presentation.ui.util

import android.os.SystemClock
import android.view.View

class SafeDoubleClick(
    private var defaultInterval: Int = 1000,
    private val onSingleCLick: (View) -> Unit
) : View.OnClickListener {
    private var lastTimeClicked: Long = 0
    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) {
            return
        }
        lastTimeClicked = SystemClock.elapsedRealtime()
        onSingleCLick(v)
    }
}