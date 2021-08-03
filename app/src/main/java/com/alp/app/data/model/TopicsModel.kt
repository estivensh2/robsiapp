/*
 * *
 *  * Created by estiven on 3/08/21, 3:05 p. m.
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 30/07/21, 7:02 p. m.
 *
 */

package com.alp.app.data.model

import com.google.gson.annotations.SerializedName

data class TopicsModel (
    @SerializedName("id_topic")         var id_topic   : Int,
    @SerializedName("title")            var title      : String,
    @SerializedName("percentage")       var percentage : Int,
    @SerializedName("items_completed")  var items : String,
    @SerializedName("percentageTopics") var percentageTopics : Float,
    )