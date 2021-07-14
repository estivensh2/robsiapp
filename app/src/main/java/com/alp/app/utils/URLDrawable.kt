/*
 * *
 *  * Created by estiv on 10/07/21 01:20 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 10/07/21 01:20 PM
 *
 */

package com.alp.app.utils

import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable


class URLDrawable : BitmapDrawable() {
    // the drawable that you need to set, you could set the initial drawing
    // with the loading image if you need to
    var drawable: Drawable? = null

    override fun draw(canvas: Canvas) {
        // override the draw to facilitate refresh function later
        if (drawable != null) {
            drawable!!.draw(canvas)
        }
    }
}
