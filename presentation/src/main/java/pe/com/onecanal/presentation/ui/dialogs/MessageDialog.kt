package pe.com.onecanal.presentation.ui.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.core.content.ContextCompat
import pe.com.onecanal.R
import pe.com.onecanal.databinding.DialogFragmentMessageBinding
import pe.com.onecanal.presentation.ui.dialogs.base.BaseDialogFragment

class MessageDialog(
    private val messageDialogType: MessageDialogType,
    private val message: String?,
    private val onButtonClick: () -> Unit,
    private val confirmationBtnTxt: String = "",
) :
    BaseDialogFragment<DialogFragmentMessageBinding>() {

    override fun onCreateView(view: View) {

        if (dialog != null && dialog!!.window != null) {
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            when (messageDialogType) {
                MessageDialogType.Info -> iconIv.setImageDrawable(
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_info)
                )
                MessageDialogType.Success -> iconIv.setImageDrawable(
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_check)
                )
                MessageDialogType.Error -> iconIv.setImageDrawable(
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_cancel)
                )
                MessageDialogType.Answer -> iconIv.setImageDrawable(
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_answer)
                )
            }

            message?.let {
                messageTv.text = it
            }
            if (confirmationBtnTxt.isNotEmpty()) binding.okDlgBtn.text = confirmationBtnTxt
            okDlgBtn.setOnClickListener {
                dismiss()
                onButtonClick.invoke()
            }
        }

    }

    override fun configureDataBinding() =
        DialogFragmentMessageBinding.inflate(LayoutInflater.from(context))

}

sealed class MessageDialogType {
    object Success : MessageDialogType()
    object Info : MessageDialogType()
    object Error : MessageDialogType()
    object Answer : MessageDialogType()
}