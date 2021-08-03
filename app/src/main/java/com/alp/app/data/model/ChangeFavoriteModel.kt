/*
 * *
 *  * Created by estiven on 3/08/21, 3:05 p. m.
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 21/07/21, 4:21 p. m.
 *
 */

package com.alp.app.data.model

import com.google.gson.annotations.SerializedName

data class ChangeFavoriteModel(
    @SerializedName("response") var response: Int
)