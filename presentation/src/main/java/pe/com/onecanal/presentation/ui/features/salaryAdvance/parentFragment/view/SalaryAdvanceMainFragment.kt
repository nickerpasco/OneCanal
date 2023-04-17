package pe.com.onecanal.presentation.ui.features.salaryAdvance.parentFragment.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import pe.com.onecanal.R
import pe.com.onecanal.databinding.FragmentSalaryAdvanceMainBinding
import pe.com.onecanal.presentation.ui.base.BaseFragmentWithViewModel
import pe.com.onecanal.presentation.ui.features.mainDrawer.view.MainDrawerActivity
import pe.com.onecanal.presentation.ui.features.salaryAdvance.parentFragment.viewmodel.SalaryAdvanceViewModel

@AndroidEntryPoint
class SalaryAdvanceMainFragment :
    BaseFragmentWithViewModel<FragmentSalaryAdvanceMainBinding, SalaryAdvanceViewModel>() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (requireActivity() as OnBackPressedDispatcherOwner).onBackPressedDispatcher.addCallback(
            viewLifecycleOwner
        ) {
            NavHostFragment.findNavController(requireParentFragment()).popBackStack()
        }

        viewModel = ViewModelProvider(this)[SalaryAdvanceViewModel::class.java]
        observeShowStepViewFlag()
        observeStepForStepperView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun observeShowStepViewFlag() {
        viewModel.showStepperView.observe(viewLifecycleOwner) {
            if (!it)
                binding.apply {
                    steeper.visibility = View.GONE
                    ivTitle.visibility = View.GONE
                } else binding.apply {
                steeper.visibility = View.VISIBLE
                ivTitle.visibility = View.VISIBLE
            }
        }
    }

    private fun observeStepForStepperView() {
        viewModel.stepViewNumber.observe(this.viewLifecycleOwner) {
            binding.steeper.setStepAt(it)
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainDrawerActivity).showTopBar(true)
    }

    override fun configureViewModel(): SalaryAdvanceViewModel {
        val viewModel: SalaryAdvanceViewModel by viewModels()
        return viewModel
    }

    override fun configureDataBinding() = FragmentSalaryAdvanceMainBinding.inflate(layoutInflater)
    override val configureViewModelBindingVariable: Int
        get() = R.layout.fragment_salary_advance_main
}