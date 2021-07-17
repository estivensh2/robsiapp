/*
 * *
 *  * Created by estiv on 16/07/21, 12:16 a. m.
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 16/07/21, 12:16 a. m.
 *
 */

package com.alp.app.data.model

import com.google.gson.annotations.SerializedName

data class AddReplyModel(
    @SerializedName("response") var response: Int
)