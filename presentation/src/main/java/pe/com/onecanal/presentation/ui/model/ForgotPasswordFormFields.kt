package pe.com.onecanal.presentation.ui.model

import pe.com.onecanal.R
import pe.com.onecanal.presentation.ui.util.FormField

/**
 * Created by Sergio Carrillo Diestra on 18/02/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
data class ForgotPasswordFormFields(
    val documentType: FormField = FormField(
        emptyResourceId = R.string.select_document_type,
    ),
    val documentNumber: FormField = FormField(
        emptyResourceId = R.string.digit_document_numer,
        hintResourceId = R.string.document_number
    )
) : BaseFormFields() {
    override fun fieldsAreValid() = (documentType.fieldIsValid() && documentNumber.fieldIsValid())
}