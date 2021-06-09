/*
 * EHOALAELEALAE
 */

package com.alp.app.utils

import android.content.Context
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.alp.app.R
import java.util.regex.Pattern

class Functions(val context: Context) {

    fun validarCorreo(email: String): Boolean {
        return Pattern.compile(
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        ).matcher(email).matches()
    }

    fun enabledButton(enabled: Boolean, button: Button){
        if(enabled){
            button.isEnabled = true
            button.setTextColor(ContextCompat.getColor(context, R.color.colorAmarilloClaro))
        } else {
            button.isEnabled = false
            button.setTextColor(ContextCompat.getColor(context, R.color.colorGrisClaroMedio))
        }
    }

    fun showHideProgressBar(showHide: Boolean, progress: LottieAnimationView){
        if(showHide){
            progress.visibility = android.view.View.VISIBLE
        } else {
            progress.visibility = android.view.View.GONE
        }
    }
}