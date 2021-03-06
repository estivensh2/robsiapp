package com.alp.app.servicios


import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.ProgressBar

object ProgressDialogo{

    private lateinit var dialogBuilder: AlertDialog.Builder
    private lateinit var alertDialog: AlertDialog
    private lateinit var pDialog: ProgressBar

    fun mostrar(ctx:Context){
        // instantiating the lateint objects
        dialogBuilder= AlertDialog.Builder(ctx)
        pDialog= ProgressBar(ctx)

        // setting up the dialog
        dialogBuilder.setCancelable(false)
        dialogBuilder.setView(pDialog)
        alertDialog=dialogBuilder.create()

        // magic of transparent background goes here
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        // setting the alertDialog's BackgroundDrawable as the color resource of any color with 1% opacity
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#00141414")))

        // finally displaying the Alertdialog containging the ProgressBar
        alertDialog.show()

    }


    fun ocultar(){
        try {
            if(alertDialog.isShowing){
                alertDialog.dismiss()
            }
        } catch (e: UninitializedPropertyAccessException) {
            //  Log.d("TAG","AlertDialog UninitializedPropertyAccessException")
        }
    }



}