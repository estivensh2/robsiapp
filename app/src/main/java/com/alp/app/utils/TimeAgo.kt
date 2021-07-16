/*
 * *
 *  * Created by estiv on 14/07/21, 3:57 p. m.
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 14/07/21, 3:57 p. m.
 *
 */

package com.alp.app.utils

import android.util.Log
import java.sql.Timestamp
import java.util.*
import java.util.concurrent.TimeUnit

private const val SECOND_MILLIS = 1000
private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
private const val DAY_MILLIS = 24 * HOUR_MILLIS

object TimeAgo {

    fun timeAgo(date: Date): String {
        var time = date.time
        if (time < 1000000000000L) {
            time *= 1000
        }
        val now = currentDate().time
        if (time > now || time <= 0) {
            return "En el futuro"
        }
        val diff = now - time
        return when {
            diff < MINUTE_MILLIS -> "Justo ahora"
            diff < 2 * MINUTE_MILLIS -> "hace un minuto"
            diff < 60 * MINUTE_MILLIS -> "${diff / MINUTE_MILLIS} minutos atrás"
            diff < 2 * HOUR_MILLIS -> "hace una hora"
            diff < 24 * HOUR_MILLIS -> "${diff / HOUR_MILLIS} horas atrás"
            diff < 48 * HOUR_MILLIS -> "ayer"
            else -> "${diff / DAY_MILLIS} días atrás"
        }
    }

    private fun currentDate(): Date {
        val calendar = Calendar.getInstance()
        return calendar.time
    }
}