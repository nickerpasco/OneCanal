package pe.com.onecanal.framework.hilt.modules

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import pe.com.onecanal.R
import pe.com.onecanal.domain.utilsreniec.Contenido

class MessageDialojM {



     fun  showMensajes( mensaje:String ,context:Activity) {

        val builder = AlertDialog.Builder(context)
        val dialogView = context.layoutInflater.inflate(R.layout.dialog_fragment_message, null)

        builder.setView(dialogView)

        val dialog = builder.create()
        dialog.show()

       // dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
       // dialog.window?.setGravity(Gravity.BOTTOM)

        val btnsalir = dialog.findViewById<Button>(R.id.okDlgBtn)
        val messageTv = dialog.findViewById<TextView>(R.id.messageTv)
        val conIv = dialog.findViewById<ImageView>(R.id.iconIv)

        conIv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_info))
        messageTv.setText(mensaje);

        btnsalir.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}