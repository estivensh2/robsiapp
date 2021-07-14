/*
 * *
 *  * Created by estiv on 10/07/21 03:23 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 10/07/21 03:23 PM
 *
 */

package com.alp.app.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Html.ImageGetter
import com.alp.app.BuildConfig


private class ResouroceImageGetter(context: Context) : ImageGetter {
    private val context: Context
    override fun getDrawable(source: String): Drawable {
        val path: Int = context.getResources().getIdentifier(
            source, "drawable", BuildConfig.APPLICATION_ID
        )
        val drawable: Drawable = context.getResources().getDrawable(path)
        drawable.setBounds(
            0, 0,
            drawable.intrinsicWidth,
            drawable.intrinsicHeight
        )
        return drawable
    }

    init {
        this.context = context
    }
}