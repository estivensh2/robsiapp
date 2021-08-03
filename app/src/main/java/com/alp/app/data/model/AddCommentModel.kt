/*
 * *
 *  * Created by estiven on 3/08/21, 3:05 p. m.
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 14/07/21, 10:20 p. m.
 *
 */

package com.alp.app.data.model

import com.google.gson.annotations.SerializedName

data class AddCommentModel(
    @SerializedName("response") var response: Int
)