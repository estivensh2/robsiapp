/*
 * *
 *  * Created by estiv on 3/07/21 09:56 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 3/07/21 09:55 PM
 *
 */

package com.alp.app.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.media.MediaPlayer
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.alp.app.R
import com.alp.app.singleton.PreferencesSingleton
import com.google.android.material.textfield.TextInputEditText
import org.jsoup.Jsoup
import java.text.SimpleDateFormat
import java.util.*
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

    fun TextInputEditText.onChange(cb: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { cb(s.toString()) }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    fun copyToClipboard(text: CharSequence){
        val clipboard = ContextCompat.getSystemService(context, ClipboardManager::class.java)
        val clip = ClipData.newPlainText("label",text)
        clipboard?.setPrimaryClip(clip)
    }

    fun converterDate(date: String): Boolean {
        val locale = Locale("es", "CO")
        val calendar = Calendar.getInstance()
        val inputFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", locale)
        val outputFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", locale)
        val parsedDate: Date = inputFormat.parse(date)!!
        val oneDate = outputFormat.format(parsedDate)
        val simpleDF = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", locale)
        calendar.add(Calendar.MONTH, 1)
        val twoDate = simpleDF.format(calendar.time)
        return oneDate < twoDate
    }

    fun compareDates(dateStart: String, dateEnd: String): Boolean {
        val locale = Locale("es", "CO")
        val simpleDateFormat  = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", locale)
        val oneDate = simpleDateFormat.parse(dateStart)!!
        val secondDate = simpleDateFormat.parse(dateEnd)!!
        return simpleDateFormat.format(oneDate) != simpleDateFormat.format(secondDate)
    }

    fun enabledButton(enabled: Boolean, button: Button){
        if(enabled){
            button.isEnabled = true
            button.setTextColor(ContextCompat.getColor(context, R.color.blue_700))
            button.setBackgroundColor(ContextCompat.getColor(context, R.color.green_500))
        } else {
            button.isEnabled = false
            button.setTextColor(ContextCompat.getColor(context, R.color.gray_200))
            button.setBackgroundColor(ContextCompat.getColor(context, R.color.gray_100))
        }
    }

    fun playSound(sound: Int) {
        val soundSharedPreferences = PreferencesSingleton.read("enabled_sound", false)
        if(soundSharedPreferences!!){
            val mediaPlayer = MediaPlayer.create(context, sound)
            mediaPlayer.start()
        }
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

    fun decodeHtml(html: String?): String? {
        return Jsoup.parse(html).text()
    }

    fun convertDateToLong(date: String): Date? {
        val locale = Locale("es", "CO")
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", locale)
        return simpleDateFormat.parse(date)
    }
}