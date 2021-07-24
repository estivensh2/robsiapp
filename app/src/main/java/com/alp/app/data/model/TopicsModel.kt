/*
 * *
 *  * Created by estiv on 3/07/21 09:56 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 29/06/21 02:01 PM
 *
 */

package com.alp.app.data.model

import com.google.gson.annotations.SerializedName

data class TopicsModel (
    @SerializedName("id_topic")        var id_topic   : Int,
    @SerializedName("title")           var title      : String,
    @SerializedName("percentage")      var percentage : Int,
    @SerializedName("items_completed") var items : String
    )