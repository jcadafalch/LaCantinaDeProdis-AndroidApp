package cat.copernic.prodis.lacantinadeprodis.utils

import android.app.AlertDialog
import android.content.Context
import cat.copernic.prodis.lacantinadeprodis.R

class utils (){

    fun showAlert(title: String, msg: String, context: Context ?= null){
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(msg)
        builder.setPositiveButton(R.string.acceptar, null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}