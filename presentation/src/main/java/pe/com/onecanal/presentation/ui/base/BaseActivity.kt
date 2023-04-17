package pe.com.onecanal.presentation.ui.base

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import pe.com.onecanal.R
import pe.com.onecanal.data.network.endpoints.LocalDataSource
import pe.com.onecanal.domain.entity.Failure
import pe.com.onecanal.presentation.ui.dialogs.ConfirmationDialog
import pe.com.onecanal.presentation.ui.dialogs.LoadingDialog
import pe.com.onecanal.presentation.ui.dialogs.MessageDialog
import pe.com.onecanal.presentation.ui.dialogs.MessageDialogType
import pe.com.onecanal.presentation.ui.model.ErrorUI
import javax.inject.Inject


abstract class BaseActivity<VBinding : ViewDataBinding, ViewModelType : ViewModel> :
    AppCompatActivity() {

    protected abstract val viewModel: ViewModelType
    protected abstract val dataBindingViewModel: Int
    protected abstract val binding: VBinding

    @Inject
    lateinit var localDataSource: LocalDataSource

    private var loadingDialog: LoadingDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        setContentView(binding.root)
        binding.setVariable(dataBindingViewModel, viewModel)
        binding.lifecycleOwner=this
        binding.executePendingBindings()
        setupUI(binding.root)
        observeUserIntentStates()
    }

    private fun observeUserIntentStates() {
        lifecycleScope.launch { handleIntentStates() }
    }

    open suspend fun handleIntentStates() {}

    protected fun stringRes(resource: Int): String {
        return getString(resource)
    }

    protected fun showSnackBar(
        rootView: View,
        contentText: String?,
        duration: Int = Snackbar.LENGTH_LONG
    ) {
        Snackbar.make(rootView, contentText ?: "error desconocido", duration).show()
    }

    protected open fun handleUseCaseFailureFromBase(failure: Failure): ErrorUI {
        logError("handleUseCaseFailureFromBase: $failure")
        return when (failure) {
            is Failure.UnauthorizedOrForbidden -> {
                //DESAHABILITADO PORQUE EL BACKEND ESTA DEVOLVIENDO 403 CUANDO EL DOCUMENTO ES INCORRECTO TE MANDA AL LOGIN CUANDO NO DEBERIA
//                showToasLong(getString(R.string.expiredSession))
//                lifecycleScope.launch {
//                    localDataSource.clearUserSession()
//                    startNewActivityClearStack<LoginActivity>()
//                }
                return ErrorUI(failure.errorMessage, failure.code)
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
            is Failure.TimeOut -> ErrorUI(
                getString(R.string.error_failure_time_out),
                failure.code
            )
            is Failure.ServerBodyError -> ErrorUI(failure.message, failure.code)
            is Failure.DataToDomainMapperFailure -> ErrorUI(
                getString(R.string.error_general), code = 0
            )
            is Failure.ServiceUncaughtFailure -> ErrorUI(
                getString(R.string.error_failure_no_network_detected),
                failure.code
            )
            Failure.ReadFromLocalDatasourceError -> ErrorUI(
                getString(R.string.error_failure_none),
                0
            )
        }
    }

    protected fun showLoadingDialog(loadingText: String? = null) {
        removeLoadingDialogFromBackStack()
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog(loadingText)
        }
        if (!loadingDialog!!.isAdded) {
            loadingDialog?.loadingText = loadingText
            loadingDialog?.show(supportFragmentManager, "loadingDialog")
        } else if (loadingDialog!!.isVisible) {
            loadingDialog?.updateLoadingText(loadingText)
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


    open fun setupUI(view: View) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (view !is EditText) {
            view.setOnTouchListener { v, event ->
                hideSoftKeyboard(this)
                false
            }
        }

        //If a layout container, iterate over children and seed recursion.
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                setupUI(innerView)
            }
        }
    }

    open fun hideSoftKeyboard(activity: Activity) {
        val inputMethodManager = activity.getSystemService(
            INPUT_METHOD_SERVICE
        ) as InputMethodManager
        if (inputMethodManager.isAcceptingText) {
            inputMethodManager.hideSoftInputFromWindow(
                activity.currentFocus!!.windowToken,
                0
            )
        }
    }

    protected fun showMessageDialog(
        dialogType: MessageDialogType,
        message: String,
        isCancelable: Boolean = true,
        confirmationBtnTxt: String = "",
        callback: () -> Unit
    ) {
        MessageDialog(dialogType, message, callback, confirmationBtnTxt).apply { this.isCancelable = isCancelable}.show(
            supportFragmentManager,
            "MessageDialog"
        )
    }

    protected fun showConfirmationDialog(
        message: String,
        confirmBtnTxt: String,
        cancelBtnTxt: String,
        callback: () -> Unit
    ) {
        ConfirmationDialog(message, confirmBtnTxt, cancelBtnTxt) {
            callback.invoke()
        }.show(supportFragmentManager, "confirmationDialog")
    }

    protected fun showToasShort(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    protected fun showToasLong(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun logError(errorMessage: Any?) {
        Log.e(this.javaClass.simpleName, "ERROR: $errorMessage")
    }

    protected fun logDebug(debugMessage: Any?) {
        Log.e(this.javaClass.simpleName, "DEBUG: $debugMessage")
    }

    protected fun logInfo(infoMessage: Any?) {
        Log.i(this.javaClass.simpleName, "INFO: $infoMessage")
    }
}