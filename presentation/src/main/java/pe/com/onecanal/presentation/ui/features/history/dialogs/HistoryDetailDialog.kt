package pe.com.onecanal.presentation.ui.features.history.dialogs

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import pe.com.onecanal.R
import pe.com.onecanal.databinding.DialogFragmentSalaryAdvanceDetailBinding
import pe.com.onecanal.domain.entity.HistoryItem
import pe.com.onecanal.domain.entity.SalaryAdvanceStatus

class HistoryDetailDialog(var historyItem: HistoryItem) : DialogFragment() {

    lateinit var binding: DialogFragmentSalaryAdvanceDetailBinding

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogFragmentSalaryAdvanceDetailBinding.inflate(LayoutInflater.from(context))
        if (dialog != null && dialog!!.window != null) {
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        }
        binding.root.background = context?.getDrawable(R.drawable.dialog_background_rounded2)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            dateTv.text = getString(R.string.amount_tranfered, historyItem.date)
            AdvanceAmountTv.text = historyItem.amount
            totalComissionsTv.text = getString(R.string.total_comissions, historyItem.feesAmount)
            realAmountToTransfetTv.text = historyItem.transferAmount
            BankAccountTv.text = historyItem.account
            PaymentPeriodTv.text = historyItem.periodName
            advanceReasonTv.text = historyItem.reason
            btnOkDlg.setOnClickListener { dismiss() }
            statusTv.text = historyItem.status.stringValue

            when (historyItem.status) {
                SalaryAdvanceStatus.REGISTERED -> {
                    statusTv.background =
                        ResourcesCompat.getDrawable(
                            requireContext().resources,
                            R.drawable.bg_btn_solicited,
                            null
                        )
                }

                SalaryAdvanceStatus.CONFIRMED -> {
                    statusTv.background =
                        ResourcesCompat.getDrawable(
                            requireContext().resources,
                            R.drawable.bg_btn_approved,
                            null
                        )
                }
                SalaryAdvanceStatus.DEPOSITED -> {
                    statusTv.background =
                        ResourcesCompat.getDrawable(
                            requireContext().resources,
                            R.drawable.bg_btn_abonado,
                            null
                        )
                    statusTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                }
                SalaryAdvanceStatus.COMPLETED -> {
                    statusTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.hinter_color_2))
                }
                SalaryAdvanceStatus.CANCELED -> {}
            }
        }

    }

    override fun onResume() {
        super.onResume()
        val window = dialog!!.window ?: return
        val params = window.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        window.attributes = params
    }

}