package pe.com.onecanal.presentation.ui.dialogs.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import pe.com.onecanal.R

/**
 * Created by Sergio Carrillo Diestra on 7/02/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
abstract class BaseDialogFragment<VBinding : ViewBinding> : DialogFragment() {

    protected abstract fun configureDataBinding(): VBinding
    protected lateinit var binding: VBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = configureDataBinding()
        // Binding the dataBinding
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (dialog != null && dialog!!.window != null) {
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        }
        binding = configureDataBinding()
        binding.root.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.dialog_background_rounded2)
        onCreateView(binding.root)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val window = dialog!!.window ?: return
        val params = window.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        window.attributes = params
    }

    open fun onCreateView(view: View) {
//CALL THIS METHOD IN CHILD FOR MORE CLEAN CODE
    }

}