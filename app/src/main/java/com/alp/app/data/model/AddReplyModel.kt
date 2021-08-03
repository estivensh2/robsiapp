/*
 * *
 *  * Created by estiven on 3/08/21, 3:05 p. m.
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 16/07/21, 12:20 a. m.
 *
 */

package com.alp.app.data.model

import com.google.gson.annotations.SerializedName

data class AddReplyModel(
    @SerializedName("response") var response: Int
)