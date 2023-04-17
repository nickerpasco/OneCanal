package pe.com.onecanal.presentation.ui.dialogs

import android.os.Build
import android.text.Html
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.view.ViewGroup
import pe.com.onecanal.databinding.DialogFragmentDocumentConfirmationBinding
import pe.com.onecanal.presentation.ui.dialogs.base.BaseDialogFragment

class DocumentConfirmationDialog(
    private val title: String = "",
    private val terms: String,
    private val type: DocType = DocType.Terms,
    private val onButtonClick: (Boolean, DocType) -> Unit
) :
    BaseDialogFragment<DialogFragmentDocumentConfirmationBinding>() {

    override fun onCreateView(view: View) {
        binding.closeTv.setOnClickListener { dismiss() }

        binding.titleDialog.text = title

        binding.termsAndConditionsTv.movementMethod = ScrollingMovementMethod()

        binding.termsAndConditionsTv.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                it.text = Html.fromHtml(terms, Html.FROM_HTML_OPTION_USE_CSS_COLORS);
            } else {
                it.text = Html.fromHtml(terms);
            }
        }

        binding.btnCancelDlg.setOnClickListener {
            onButtonClick(false, type)
            dismiss()
        }
        binding.btnOkDlg.setOnClickListener {
            onButtonClick(true, type)
            dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        val window = dialog!!.window ?: return
        val params = window.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        window.attributes = params
    }

    override fun configureDataBinding() = DialogFragmentDocumentConfirmationBinding.inflate(layoutInflater)
}

sealed class DocType {
    object Terms : DocType()
    object Contract : DocType()
    object Other : DocType()
}