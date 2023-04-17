package pe.com.onecanal.presentation.ui.dialogs

import android.os.Bundle
import android.view.View
import pe.com.onecanal.databinding.DialogFragmentConfirmationBinding
import pe.com.onecanal.presentation.ui.dialogs.base.BaseDialogFragment

class ConfirmationDialog(
    private val message: String?,
    private val confirmationBtnTxt: String = "",
    private val cancelBtnTxt: String = "",
    private val onButtonClick: () -> Unit,
) : BaseDialogFragment<DialogFragmentConfirmationBinding>() {

    override fun onCreateView(view: View) {
        if (confirmationBtnTxt.isNotEmpty()) binding.btnOkDlg.text = confirmationBtnTxt
        if (cancelBtnTxt.isNotEmpty()) binding.btnCancelDlg.text = cancelBtnTxt
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            message?.let {
                tvMessage.text = it
            }
            btnOkDlg.setOnClickListener {
                dismiss()
                onButtonClick.invoke()
            }
            btnCancelDlg.setOnClickListener {
                dismiss()
            }
        }
    }

    override fun configureDataBinding() = DialogFragmentConfirmationBinding.inflate(layoutInflater)
}