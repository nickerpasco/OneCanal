package pe.com.onecanal.presentation.ui.util


import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

inline fun <T : ViewDataBinding> AppCompatActivity.dataBinding(
    crossinline bindingInflater: (LayoutInflater) -> T,
): Lazy<T> {
    return lazy(LazyThreadSafetyMode.NONE) {
        bindingInflater(layoutInflater)
    }
}

inline fun <T : ViewDataBinding> Fragment.dataBinding(
    crossinline bindingInflater: (LayoutInflater) -> T,
): Lazy<T> {
    return lazy(LazyThreadSafetyMode.NONE) {
        bindingInflater(layoutInflater)
    }
}