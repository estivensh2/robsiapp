/*
 * *
 *  * Created by estiven on 3/08/21, 3:05 p. m.
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 2/08/21, 1:53 p. m.
 *
 */

package com.alp.app.data.model

import com.google.gson.annotations.SerializedName

data class SurveyModel(
    @SerializedName("response") val response: Int
)