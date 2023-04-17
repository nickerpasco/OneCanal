package pe.com.onecanal.presentation.ui.features.profile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import pe.com.onecanal.R
import pe.com.onecanal.domain.entity.BankAccount


/**
 * Created by Sergio Carrillo Diestra on 18/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
class BankLinearLayoutAdapter(context: Context, private val items: List<BankAccount>) :
    LinearLayout(context) {
    init {

        orientation = VERTICAL
        for (item in items) {
            val listItem = LayoutInflater.from(context).inflate(R.layout.bank_account_item_list, null)
            (listItem!!.findViewById(R.id.bankNameTv) as TextView)
                .text= item.shortName
            (listItem.findViewById(R.id.cardNumberTv) as TextView)
                .text= item.number
            this.addView(listItem)
        }
    }

}