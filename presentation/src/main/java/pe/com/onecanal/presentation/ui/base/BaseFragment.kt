package pe.com.onecanal.presentation.ui.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import pe.com.onecanal.R
import pe.com.onecanal.data.network.endpoints.LocalDataSource
import pe.com.onecanal.domain.entity.Failure
import pe.com.onecanal.presentation.ui.dialogs.LoadingDialog
import pe.com.onecanal.presentation.ui.dialogs.MessageDialog
import pe.com.onecanal.presentation.ui.dialogs.MessageDialogType
import pe.com.onecanal.presentation.ui.extensions.startNewActivityClearStack
import pe.com.onecanal.presentation.ui.features.login.view.LoginActivity
import pe.com.onecanal.presentation.ui.model.ErrorUI
import javax.inject.Inject

abstract class BaseFragment<ViewDataBindingClass : ViewDataBinding> : Fragment() {
    protected abstract fun configureDataBinding(): ViewDataBindingClass
    protected lateinit var navController: NavController
    private var loadingDialog: LoadingDialog? = null
    protected lateinit var binding: ViewDataBindingClass

    @Inject
    lateinit var localDataSource: LocalDataSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = configureDataBinding()
        // Binding the dataBinding
    }

    protected fun showLoadingDialog(loadingText: String? = null) {
        removeLoadingDialogFromBackStack()
        if (activity != null) {
            if (loadingDialog == null) {
                loadingDialog = LoadingDialog(loadingText)
            }
            if (!loadingDialog!!.isAdded) {

                loadingDialog?.loadingText = loadingText
                loadingDialog?.show(childFragmentManager, "loadingDialog")
            } else if (loadingDialog!!.isVisible) {
                loadingDialog?.updateLoadingText(loadingText)
            }
        }
    }

    protected fun closeLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog?.dismiss()
        }
    }

    private fun removeLoadingDialogFromBackStack() {
        loadingDialog?.dismiss()
    }

    //region SnackBar
    protected fun showSnackBar(
        rootView: View,
        contentText: Any,
        duration: Int = Snackbar.LENGTH_LONG
    ) {
        val text = when (contentText) {
            is String -> contentText
            is Int -> getString(contentText)
            else -> ""
        }
        Snackbar.make(rootView, text, duration).show()
    }

    protected fun showSnackBar(message: String) {
        Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_LONG
        ).show()
    }


    protected fun showToast(message: Any?) {
        val textToShow = when (message) {
            is Int -> getString(message)
            is String -> message
            else -> {
                ""
            }
        }
        Toast.makeText(requireContext(), textToShow, Toast.LENGTH_LONG).show()
    }

    protected fun showToasLong(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    protected fun hideKeyboard() {
        try {
            val imm =
                view?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(requireView().windowToken, 0)

        } catch (e: Exception) {
            showToast(e.message)
        }
    }

    protected fun showMessageDialog(
        dialogType: MessageDialogType,
        message: String,
        confirmationBtnTxt: String = "",
        isCancelable: Boolean = true,
        callback: () -> Unit = {},
    ) {
        if (activity != null && !requireActivity().isFinishing)
            MessageDialog(dialogType, message, callback, confirmationBtnTxt).apply { this.isCancelable = isCancelable}.show(
                parentFragmentManager,
                "MessageDialog"
            )
    }

    protected fun logError(message: Any?) {
        Log.e(this.javaClass.simpleName, "LOG ERROR: $message")
    }

    protected fun logDebug(message: Any?) {
        Log.d(this.javaClass.simpleName, "LOG DEBUG: $message")
    }

    protected open fun handleUseCaseFailureFromBase(failure: Failure): ErrorUI {
        logError("handleUseCaseFailureFromBase: $failure")
        return when (failure) {
            is Failure.UnauthorizedOrForbidden -> {
                showToasLong(getString(R.string.expiredSession))
                lifecycleScope.launch {
                    localDataSource.clearUserSession()
                    requireActivity().startNewActivityClearStack<LoginActivity>()
                }
                ErrorUI(failure.errorMessage, failure.code)
            }

            is Failure.None -> ErrorUI(getString(R.string.error_failure_none), failure.code)
            is Failure.NetworkConnectionLostSuddenly -> ErrorUI(
                getString(R.string.error_failure_network_connection_lost_suddenly),
                failure.code
            )
            is Failure.NoNetworkDetected -> ErrorUI(
                getString(R.string.error_failure_no_network_detected),
                failure.code
            )
            is Failure.SSLError -> ErrorUI(getString(R.string.error_failure_ssl), failure.code)
            is Failure.TimeOut -> ErrorUI(getString(R.string.error_failure_time_out), failure.code)
            is Failure.ServerBodyError -> ErrorUI(failure.message, failure.code)
            is Failure.DataToDomainMapperFailure -> ErrorUI(
                getString(R.string.error_general), code = 0
            )
            is Failure.ServiceUncaughtFailure -> ErrorUI(
                getString(R.string.error_failure_none),
                failure.code
            )
            Failure.ReadFromLocalDatasourceError -> ErrorUI(
                getString(R.string.error_failure_none),
                0
            )
        }
    }

//    //force Logout due to 401 or 403
//    protected fun forceLogout(it: Boolean) {
//        if(it){
//            showToast(R.string.log_out_by_other_session)
//            NotificationUtil.dismissNotification(requireContext())
//            preferences.deleteAccessToken()
//            preferences.deleteDriverModel()
//            activity?.startNewActivityClearStack<SplashActivity>()
//        }
//    }

}