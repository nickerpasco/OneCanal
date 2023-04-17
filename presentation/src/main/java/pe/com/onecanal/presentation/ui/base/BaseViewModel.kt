package pe.com.onecanal.presentation.ui.base

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Created by Sergio Carrillo Diestra on 10/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
abstract class BaseViewModel<T> : ViewModel(){

    //Shows, hide, error message view.
    private val _showErrorCause = MutableLiveData(false)
    val showErrorCause: LiveData<Boolean>
        get() = _showErrorCause

    // The resource default value of the error or any error(Exception, server side, etc).
    private val _errorCause = MutableLiveData<Any>()
    val errorCause: LiveData<Any>
        get() = _errorCause

    // Shows or hide and empty view layout if the view have it
    private val _showEmptyView = MutableLiveData(false)
    val showEmptyView: LiveData<Boolean>
        get() = _showEmptyView

    // Fired when a failure is of type Unauthorized
    private val _forceLogOut = MutableLiveData<Boolean>()
    val forceLogOut: LiveData<Boolean>
        get() = _forceLogOut


    protected fun shouldShowEmptyView(show: Boolean?) {
        _showEmptyView.value = show
    }


    protected fun showErrorCause(show: Boolean) {
        _showErrorCause.value = show
    }

    protected fun setError(cause: Any) {
        if (cause is String) {
            logError(cause)
        }
        shouldShowEmptyView(false)
        showErrorCause(true)
        _errorCause.value = cause
    }


    protected fun logError(errorMessage: Any?) {
        Log.e(this.javaClass.simpleName, "ERROR: $errorMessage")
    }

    protected fun logDebug(debugMessage: Any?) {
        Log.e(this.javaClass.simpleName, "DEBUG: $debugMessage")
    }

    protected fun logInfo(infoMessage: Any?) {
        Log.i(this.javaClass.simpleName, "INFO: $infoMessage")
    }
}