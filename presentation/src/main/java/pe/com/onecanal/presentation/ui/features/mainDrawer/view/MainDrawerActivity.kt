package pe.com.onecanal.presentation.ui.features.mainDrawer.view

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pe.com.onecanal.BR
import pe.com.onecanal.R
import pe.com.onecanal.databinding.ActivityMainDrawerBinding
import pe.com.onecanal.domain.entity.BankAccount
import pe.com.onecanal.domain.entity.Failure
import pe.com.onecanal.domain.entity.UserProfileData
import pe.com.onecanal.presentation.ui.base.BaseActivity
import pe.com.onecanal.presentation.ui.dialogs.ConfirmationDialog
import pe.com.onecanal.presentation.ui.dialogs.MessageDialogType
import pe.com.onecanal.presentation.ui.extensions.startNewActivityClearStack
import pe.com.onecanal.presentation.ui.features.login.view.LoginActivity
import pe.com.onecanal.presentation.ui.features.mainDrawer.intent.MainDrawerIntent
import pe.com.onecanal.presentation.ui.features.mainDrawer.intent.MainDrawerIntentConfig
import pe.com.onecanal.presentation.ui.features.mainDrawer.viewmodel.MainDrawerViewModel
import pe.com.onecanal.presentation.ui.features.salaryAdvance.stepTwo.intent.SalaryAdvanceStepTwoIntent
import pe.com.onecanal.presentation.ui.util.dataBinding

@AndroidEntryPoint
class MainDrawerActivity : BaseActivity<ActivityMainDrawerBinding, MainDrawerViewModel>(),
    View.OnClickListener,
    MainDrawerIntentConfig.MainDrawerIntentCallback {
    private var navHostFragment = NavHostFragment()
//    private lateinit var navController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        printUserDataFromDataStore()
        configureClickListeners()
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_mobile_home) as NavHostFragment
//        navController = findNavController(R.id.nav_mobile_home)
        navView.setupWithNavController(navHostFragment.navController)
        observeShowStepViewFlag()
        observeStepForStepperView()
    }

    private fun observeShowStepViewFlag() {
        viewModel.showStepperView.observe(this) {
            if (!it)
                binding.appBarMainDrawer.contentMainDrawer.apply {
                    steeper.visibility = View.GONE
                    ivTitle.visibility = View.GONE
                } else binding.appBarMainDrawer.contentMainDrawer.apply {
                steeper.visibility = View.VISIBLE
                ivTitle.visibility = View.VISIBLE
            }
        }
    }

    private fun observeStepForStepperView() {
        viewModel.stepViewNumber.observe(this) {
            binding.appBarMainDrawer.contentMainDrawer.steeper.setStepAt(it)
        }
    }


    private fun configureClickListeners() {
        binding.apply {
            appBarMainDrawer.contentMainDrawer.myTopBar.btnDrawerMenu.setOnClickListener {
                drawerLayout.openDrawer(Gravity.LEFT)
            }
            signOutBtn.setOnClickListener(this@MainDrawerActivity)
        }
    }

    override suspend fun handleIntentStates() {
        viewModel.intentState.collect {
            MainDrawerIntentConfig.instance(this@MainDrawerActivity).collect(it)
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.signOutBtn -> {
                showConfirmationDialog(
                    getString(R.string.wantToSignOut),
                    getString(R.string.accept),
                    getString(R.string.cancel)
                ) { signOut() }
            }
        }
    }

    private fun signOut() {
        lifecycleScope.launch {
            viewModel.userIntent.send(MainDrawerIntent.ClearUserSession)
        }
    }

    private fun printUserDataFromDataStore() {
        lifecycleScope.launch {
            viewModel.userIntent.send(MainDrawerIntent.GetUserSession)
        }
    }

    fun showTopBar(it: Boolean) {
        binding.apply {
            if (!it) {
                appBarMainDrawer.contentMainDrawer.myTopBar.myTopBar.visibility = View.GONE
                appBarMainDrawer.contentMainDrawer.steeper.visibility = View.GONE
                appBarMainDrawer.contentMainDrawer.ivTitle.visibility = View.GONE
            }
            else {
                appBarMainDrawer.contentMainDrawer.myTopBar.myTopBar.visibility = View.VISIBLE
                appBarMainDrawer.contentMainDrawer.steeper.visibility = View.VISIBLE
                appBarMainDrawer.contentMainDrawer.ivTitle.visibility = View.VISIBLE
            }
        }
    }

    fun showStepper(it: Boolean) {
        binding.apply {
            appBarMainDrawer.contentMainDrawer.myTopBar.myTopBar.visibility = View.VISIBLE
            if (!it) {
                appBarMainDrawer.contentMainDrawer.steeper.visibility = View.GONE
                appBarMainDrawer.contentMainDrawer.ivTitle.visibility = View.GONE
            }
            else {
                appBarMainDrawer.contentMainDrawer.steeper.visibility = View.VISIBLE
                appBarMainDrawer.contentMainDrawer.ivTitle.visibility = View.VISIBLE
            }
        }
    }

    override fun onUserSessionDataFetched(userProfileData: UserProfileData?) {
        closeLoadingDialog()
        val headerLayout = binding.navView.getHeaderView(0)
        val tvCustomerName = headerLayout.findViewById<TextView>(R.id.drawerUserNameTv)
        tvCustomerName.text = userProfileData?.getShorFullName() ?: ""
    }

    override fun onUserSessionCleared() {
        startNewActivityClearStack<LoginActivity>()
    }

    override fun onLoading() {
        showLoadingDialog()
    }

    override fun onError(error: Failure) {
        closeLoadingDialog()
        handleUseCaseFailureFromBase(error)
    }

    override val dataBindingViewModel=BR.mainDrawerViewModel
    override val viewModel: MainDrawerViewModel by viewModels()
    override val binding by dataBinding(ActivityMainDrawerBinding::inflate)
}