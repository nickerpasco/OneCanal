package pe.com.onecanal.presentation.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel

/**
 * This base fragment help us to reduce boiler plate code and automatize task like observe always to forceLogOut live data.
 * Also, extends [BaseFragment] for make use of Progress Loading and Snack Bar.
 */

abstract class BaseFragmentWithViewModel<ViewDataBindingClass : ViewDataBinding, ViewModelType : ViewModel> :
    BaseFragment<ViewDataBindingClass>() {

    protected lateinit var viewModel: ViewModelType

    protected abstract fun configureViewModel(): ViewModelType
    protected abstract val configureViewModelBindingVariable: Int
    private lateinit var rootView: View
    private fun init() {
        // Binding the viewModel
        viewModel = configureViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = binding.root
        binding.setVariable(configureViewModelBindingVariable, viewModel)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.executePendingBindings()
        if (savedInstanceState == null)
            onCreateView(view)
    }

    /**
     * We can override this function instead of [onViewCreated].
     * also this will be always listening to forceLogOut liveData.
     */
    open fun onCreateView(view: View) {

    }
}