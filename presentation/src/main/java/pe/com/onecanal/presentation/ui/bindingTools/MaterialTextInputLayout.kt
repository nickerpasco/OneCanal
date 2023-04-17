package pe.com.onecanal.presentation.ui.bindingTools

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

/**
 * Created by Sergio Carrillo Diestra on 21/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/

@BindingAdapter("setHint")
fun setTilHint(til: TextInputLayout, hint: Any?) {
    if (hint != null && hint is String?) {
        til.hint = hint
    }
    if (hint != null && hint is Int?) {
        til.hint = til.resources.getString(hint)
    }
}

@BindingAdapter("setError")
fun setError(til: TextInputLayout, error: Int?) {
    til.error = if (error != null) til.resources.getString(error) else null
}