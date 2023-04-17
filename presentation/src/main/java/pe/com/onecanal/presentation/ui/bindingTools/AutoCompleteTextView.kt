package pe.com.onecanal.presentation.ui.bindingTools

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ListView
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener

/**
 * Created by Sergio Carrillo Diestra on 22/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
//

object AutoCompleteTextView
{

    @JvmStatic
    @BindingAdapter("entries", "itemLayout", "textViewId", requireAll = false)
    fun AutoCompleteTextView.bindAdapter(entries: List<String>, @LayoutRes itemLayout: Int?, @IdRes textViewId: Int?) {
        val adapter = when {
            itemLayout == null -> {
                ArrayAdapter(context, android.R.layout.simple_list_item_1, android.R.id.text1, entries)
            }
            textViewId == null -> {
                ArrayAdapter(context, itemLayout, entries)
            }
            else -> {
                ArrayAdapter(context, itemLayout, textViewId, entries)
            }
        }
        setAdapter(adapter)
    }

}





