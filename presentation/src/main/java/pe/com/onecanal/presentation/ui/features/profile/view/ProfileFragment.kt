package pe.com.onecanal.presentation.ui.features.profile.view

import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pe.com.onecanal.R
import pe.com.onecanal.databinding.FragmentProfileBinding
import pe.com.onecanal.domain.entity.Failure
import pe.com.onecanal.domain.entity.UserProfileData
import pe.com.onecanal.presentation.ui.base.BaseFragmentWithViewModel
import pe.com.onecanal.presentation.ui.features.mainDrawer.view.MainDrawerActivity
import pe.com.onecanal.presentation.ui.features.profile.adapter.BankLinearLayoutAdapter
import pe.com.onecanal.presentation.ui.features.profile.intent.ProfileIntent
import pe.com.onecanal.presentation.ui.features.profile.intent.ProfileIntentConfig
import pe.com.onecanal.presentation.ui.features.profile.viewmodel.ProfileViewModel

@AndroidEntryPoint
class ProfileFragment : BaseFragmentWithViewModel<FragmentProfileBinding, ProfileViewModel>(),
    ProfileIntentConfig.IntentCallback {
    var position = 0
    private lateinit var userToken: String

    override fun configureDataBinding() = FragmentProfileBinding.inflate(layoutInflater)

    override fun configureViewModel(): ProfileViewModel {
        val viewModel: ProfileViewModel by viewModels()
        return viewModel
    }

    override fun onCreateView(view: View) {
        observeUserIntentStates()
        getUserSessionLocal()
        binding.apply {
            myTobBar.btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
            changePassowrdBtn.setOnClickListener {
                findNavController().navigate(ProfileFragmentDirections.actionNavHomeToChangePasswordFragment())
            }
            editBtn.setOnClickListener {
                findNavController().navigate(ProfileFragmentDirections.actionNavHomeToEditFragment())
            }
            addBankAccount.setOnClickListener {
                findNavController().navigate(ProfileFragmentDirections.actionNavHomeToBankFragment())
            }
            layoutProfile.isVisible = true
            layoutAdditional.isVisible = false
            tabProfile.removeAllTabs()
            tabProfile.addTab(tabProfile.newTab().setText("Información\npersonal"))
            tabProfile.addTab(tabProfile.newTab().setText("Información\nadicional"))
            tabProfile.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    when(tab.position) {
                        0 -> {
                            layoutProfile.isVisible = true
                            layoutAdditional.isVisible = false
                        }
                        1 -> {
                            layoutProfile.isVisible = false
                            layoutAdditional.isVisible = true
                        }
                    }
                    position = tab.position
                }
                override fun onTabUnselected(tab: TabLayout.Tab) {

                }
                override fun onTabReselected(tab: TabLayout.Tab) {

                }
            })
        }
    }

    private fun observeUserIntentStates() {
        lifecycleScope.launch {
            viewModel.intentState.collect {
                ProfileIntentConfig.instance(this@ProfileFragment).collect(it)
            }
        }
    }

    private fun getUserDataService() {
        lifecycleScope.launch {
            viewModel.userIntent.send(ProfileIntent.GetUserDataFromRemote)
        }
    }

    private fun getUserSessionLocal() {
        lifecycleScope.launch {
            viewModel.userIntent.send(ProfileIntent.GetUserSessionFromLocal)
        }
    }

    private fun saveUserData(userProfileData: UserProfileData) {
        lifecycleScope.launch {
            viewModel.userIntent.send(ProfileIntent.UpdateUserSession(userProfileData))
        }
    }

    override fun onResume() {
        super.onResume()
        //HIDE THE TOPBAR ACCORDING TO UI REQUIREMENTS
        (activity as MainDrawerActivity).showTopBar(false)
    }

    override fun onUserSessionFromLocalFetched(userProfileData: UserProfileData?) {
        this.userToken = userProfileData?.userToken!!
        getUserDataService()
    }

    override fun onUserDataFromRemoteFetched(userProfileData: UserProfileData) {
        userProfileData.userToken = this.userToken
        printUserDataUI(userProfileData)
        saveUserData(userProfileData)
    }

    override fun onUserSessionSaved() {
        closeLoadingDialog()
    }

    private fun printUserDataUI(user: UserProfileData?) {
        binding.apply {
            user?.let {
                fullNameTv.text = user.getShorFullName()
                dniTv.text = user.documentNumber
                emailTv.text = user.email
                businessTv.text = user.business
                rucTv.text = user.ruc
                documentTypeTv.text = user.documentType
                val list = user.accounts.filter { it.active && it.confirmed }
                bankAccountLl.removeAllViews()
                bankAccountLl.addView(BankLinearLayoutAdapter(requireContext(), list))
                money.text = user.salary.toString()
                job.text = user.businessJob
                maritalState.text = user.maritalName
                address.text = user.address
            }

        }
    }

    override fun onLoading() {
        showLoadingDialog()
    }

    override fun onError(error: Failure) {
        closeLoadingDialog()
        handleUseCaseFailureFromBase(error)
    }

    override val configureViewModelBindingVariable: Int
        get() = R.layout.fragment_profile


}