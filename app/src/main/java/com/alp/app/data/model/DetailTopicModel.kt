/*
 * *
 *  * Created by estiv on 12/07/21 10:39 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 12/07/21 10:39 PM
 *
 */

package com.alp.app.data.model

import com.google.gson.annotations.SerializedName

data class DetailTopicModel(
        @SerializedName("id_detail_topic") var id_detail_topic: Int,
        @SerializedName("description")     var description: String,
        @SerializedName("level")           var level: String,
        @SerializedName("visits")          var visits: Int,
        @SerializedName("comments")        var comments: Int,
        @SerializedName("favorite")        var favorite: Int,
        @SerializedName("id_question")     var id_question: Int,
        @SerializedName("question")        var question: String,
        @SerializedName("reply")           var reply: Int,
        @SerializedName("options")         var options : List<OptionsModel>,
)