/*
 * *
 *  * Created by estiv on 15/07/21, 2:07 a. m.
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 15/07/21, 2:07 a. m.
 *
 */

package com.alp.app.data.model

import com.google.gson.annotations.SerializedName

data class DeleteCommentModel(
    @SerializedName("response") var response: Int
)