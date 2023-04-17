package pe.com.onecanal.presentation.ui.util

import android.text.InputType
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import pe.com.onecanal.presentation.ui.extensions.fixBlankSpaces
import pe.com.onecanal.presentation.ui.extensions.isValidSalaryAdvanceDecimalDecimal
import pe.com.onecanal.presentation.ui.extensions.toDoubleWithTwoDecimals

/**
 * Created by Sergio Carrillo Diestra on 20/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
class FormField constructor(
    private val hintResourceId: Int? = null,
    private val emptyResourceId: Int? = null,
    private val invalidFieldResourceId: Int? = null,
    private val imeOptionsId: Int? = null,
    var toBigValueErrorResourceId: Int? = null,
    inputTypeId: Int = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS,
    iconResourceId: Int? = null
) {

    private var _minLength: Int? = null

    // Text to listen.
    val fieldText:MutableLiveData<String> = MutableLiveData<String>("")

    private val _iconId = MutableLiveData<Int?>()
    val iconId: LiveData<Int?>
        get() = _iconId

    private val _inputType = MutableLiveData<Int>()
    val inputType: LiveData<Int>
        get() = _inputType

    private val _imeOptions = MutableLiveData<Int?>()
    val imeOptions: LiveData<Int?>
        get() = _imeOptions

    private val _maxValue = MutableLiveData<Double?>()
    val maxValue: LiveData<Double?>
        get() = _maxValue

    private val _enabled = MutableLiveData<Boolean>()
    val enabled: LiveData<Boolean>
        get() = _enabled


    private val _isAlphanumeric = MutableLiveData<Boolean>()
    val isAlphanumeric: LiveData<Boolean>
        get() = _isAlphanumeric

    private val _mandatoryLength = MutableLiveData<Int>()
    val mandatoryLength: LiveData<Int>
        get() = _mandatoryLength

    private val _showErrorBackground = MutableLiveData<Boolean>()
    val showErrorBackground: LiveData<Boolean>
        get() = _showErrorBackground

    private val _showErrorCause = MutableLiveData<Boolean>()
    val showErrorCause: LiveData<Boolean>
        get() = _showErrorCause

    val errorTextResource = MutableLiveData<Int?>()


    val hint: MutableLiveData<Any?> = MutableLiveData()

    //ths variable is for validate type doc and license
    private var typeDocKey: String? = null

    // Set up initial values for hint, icon, and InputType
    init {
        hint.value = hintResourceId
        _iconId.value = iconResourceId
        _inputType.value = inputTypeId
        _imeOptions.value = imeOptionsId
        clearErrors()
    }


    fun fieldIsValid(): Boolean {

        if (fieldText.value.isNullOrEmpty() || fieldText.value.isNullOrBlank()) {
            emptyResourceId?.let {
                setFieldErrorStatus(it)
            }
            return false
        }

        if ((inputType.value == InputType.TYPE_NUMBER_FLAG_DECIMAL || inputType.value == InputType.TYPE_CLASS_NUMBER || inputType.value == InputType.TYPE_NUMBER_FLAG_SIGNED || inputType.value == InputType.TYPE_NUMBER_VARIATION_NORMAL || inputType.value == InputType.TYPE_NUMBER_VARIATION_PASSWORD) && !fieldText.value.isValidSalaryAdvanceDecimalDecimal()) {
            invalidFieldResourceId?.let {
                setFieldErrorStatus(it)
            }
            return false
        }
       if(!validateMaxValue()) return false
        clearErrors()
        return true

/*        if (_minLength != null && _minLength!! > fieldText.value!!.length) {
            invalidFieldResourceId?.let { setFieldErrorStatus(it) }
            return false
        }

        if (mandatoryLength.value != null && fieldText.value!!.length != mandatoryLength.value) {
            invalidFieldResourceId?.let { setFieldErrorStatus(it) }
            return false
        }*/
//        if (invalidValueErrorMessage != null) {

//            if (typeDocKey != null) {
//                when (typeDocKey) {
//                    "dni" -> {
//                        if (mandatoryLength.value != null && fieldText.value!!.length != mandatoryLength.value) {
//                            setFieldErrorStatus(invalidFieldResourceId)
//                            return false
//                        }
//                        if (!fieldText.value!!.isNumeric()) {
//                            setFieldErrorStatus(invalidFieldResourceId)
//                            return false
//                        }
//                    }
//                    "ce", "pas", "license" -> {
//                        if (mandatoryLength.value != null && fieldText.value!!.length > mandatoryLength.value!!) {
//                            setFieldErrorStatus(invalidFieldResourceId)
//                            return false
//                        }
//                        if (!fieldText.value!!.validateDocument()) {
//                            setFieldErrorStatus(invalidFieldResourceId)
//                            return false
//                        }
//                    }
//                    "ptp" -> {
//                        if (mandatoryLength.value != null && fieldText.value!!.length > mandatoryLength.value!!) {
//                            setFieldErrorStatus(invalidFieldResourceId)
//                            return false
//                        }
//                        if (!fieldText.value!!.validateDocument()) {
//                            setFieldErrorStatus(invalidFieldResourceId)
//                            return false
//                        }
//                    }
//                    else -> {}
//                }

//            } else {

//            }
//        }

    }

    fun validateMaxValue(): Boolean {
        return if (maxValue.value != null &&
            fieldText.value!!.toDoubleWithTwoDecimals() > maxValue.value!!) {
            toBigValueErrorResourceId?.let { setFieldErrorStatus(it) }
            false
        } else true
    }

    fun getFixedContent() = fieldText.value?.fixBlankSpaces() ?: ""

    fun getContent() = fieldText.value?.trim() ?: ""

    fun getActualContent() = fieldText.value

    fun setFieldMandatoryLength(maxLength: Int) {
        _mandatoryLength.value = maxLength
        fieldText.value = ""
    }

    fun setInputType(typeId: Int) {
        _inputType.value = typeId
        _isAlphanumeric.value = typeId == InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
    }

    fun setFieldMinLength(minLength: Int) {
        _minLength = minLength
    }

    private fun setFieldErrorStatus(resource: Int?) {
        errorTextResource.value = resource
//        _showErrorBackground.value = true
//        _showErrorCause.value = true
    }


    fun setShowErrorCause(show: Boolean) {
        _showErrorCause.value = show
    }

    fun setShowErrorBackground(show: Boolean) {
        _showErrorBackground.value = show
    }

    fun setFieldImeOptions(imeOptionId: Int) {
        _imeOptions.value = imeOptionId
    }

    fun setMaxValue(maxValue: Double) {
        _maxValue.value = maxValue
    }

    fun isEnabled(e: Boolean) {
        _enabled.value = e
    }


    fun setTypeDocKey(key: String) {
        typeDocKey = key
    }

    fun getTypeDocKey() = typeDocKey

    fun clearErrors() {
        errorTextResource.value = null
//        _showErrorCause.value = false
//        _showErrorBackground.value = false
    }
}