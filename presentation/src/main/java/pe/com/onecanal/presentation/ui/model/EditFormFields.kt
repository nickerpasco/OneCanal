package pe.com.onecanal.presentation.ui.model

import pe.com.onecanal.R
import pe.com.onecanal.presentation.ui.util.FormField

data class EditFormFields(
    val money: FormField = FormField(
        emptyResourceId = R.string.required,
    ),
    val job: FormField = FormField(
        emptyResourceId = R.string.required,
    ),
    val maritalType: FormField = FormField(
        emptyResourceId = R.string.select_marital_type,
    ),
    val address: FormField = FormField(
        emptyResourceId = R.string.required,
    ),

) : BaseFormFields() {
    override fun fieldsAreValid() =
        (money.fieldIsValid() && job.fieldIsValid() && address.fieldIsValid())
}