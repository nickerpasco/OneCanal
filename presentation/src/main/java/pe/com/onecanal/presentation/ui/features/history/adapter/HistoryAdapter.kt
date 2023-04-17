package pe.com.onecanal.presentation.ui.features.history.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import pe.com.onecanal.R
import pe.com.onecanal.domain.entity.HistoryItem
import pe.com.onecanal.domain.entity.SalaryAdvanceStatus

/**
 * Created by Sergio Carrillo Diestra on 27/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/

class HistoryAdapter(
    private val dataSet: List<HistoryItem>,
    private val context: Context,
    val onItemClicked: (item: HistoryItem) -> Unit
) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTv: TextView = itemView.findViewById(R.id.dateTv)
        val advanceAmountTv: TextView = itemView.findViewById(R.id.advanceAmountTv)
        val statusTv: TextView = itemView.findViewById(R.id.statusTv)
        val root: ConstraintLayout = itemView.findViewById(R.id.root)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            dateTv.text = context.getString(R.string.amount_tranfered, dataSet[position].date)
            advanceAmountTv.text = dataSet[position].transferAmount
            statusTv.text = dataSet[position].status.stringValue
            root.setOnClickListener {
                onItemClicked(dataSet[position])
            }
        }
        setStatusBackground(position, holder)
    }

    private fun setStatusBackground(position: Int, holder: ViewHolder) {
        when (dataSet[position].status) {
            SalaryAdvanceStatus.REGISTERED -> {
                holder.statusTv.background =
                    ResourcesCompat.getDrawable(
                        context.resources,
                        R.drawable.bg_btn_solicited,
                        null
                    )
            }

            SalaryAdvanceStatus.CONFIRMED -> {
                holder.statusTv.background =
                    ResourcesCompat.getDrawable(
                        context.resources,
                        R.drawable.bg_btn_approved,
                        null
                    )
            }
            SalaryAdvanceStatus.DEPOSITED -> {
                holder.statusTv.background =
                    ResourcesCompat.getDrawable(
                        context.resources,
                        R.drawable.bg_btn_abonado,
                        null
                    )
                holder.statusTv.setTextColor(ContextCompat.getColor(context, R.color.white))
            }
            SalaryAdvanceStatus.COMPLETED -> {
                holder.statusTv.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.hinter_color_2
                    )
                )
            }
            SalaryAdvanceStatus.CANCELED -> {
                holder.statusTv.setTextColor(ContextCompat.getColor(context, R.color.orange))
            }
        }

    }

    override fun getItemCount() = dataSet.size

}