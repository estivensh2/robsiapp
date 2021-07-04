/*
 * *
 *  * Created by estiv on 3/07/21 09:56 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 3/07/21 09:55 PM
 *
 */

package com.alp.app.utils

import android.content.Context
import android.media.MediaPlayer
import android.widget.Button
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.alp.app.R
import java.util.regex.Pattern

class Functions(val context: Context) {

    fun validateEmail(email: String): Boolean {
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
            button.setTextColor(ContextCompat.getColor(context, R.color.yellow_500))
        } else {
            button.isEnabled = false
            button.setTextColor(ContextCompat.getColor(context, R.color.gray))
        }
    }

    fun playSound(sound: Int) {
        val mediaPlayer = MediaPlayer.create(context, sound)
        mediaPlayer.start()
    }

    fun stringRandom(list: List<String>) : String {
        val position = (0..2).random()
        return list[position]
    }

    fun showHideProgressBar(showHide: Boolean, progress: LottieAnimationView){
        if(showHide){
            progress.visibility = android.view.View.VISIBLE
        } else {
            progress.visibility = android.view.View.GONE
        }
    }
}