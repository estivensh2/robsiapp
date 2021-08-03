/*
 * *
 *  * Created by estiven on 3/08/21, 3:05 p. m.
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 1/08/21, 5:31 p. m.
 *
 */

package com.alp.app.data.model

import com.google.gson.annotations.SerializedName

class CertificateModel (
        @SerializedName("id_course")     var id_course    : Int,
        @SerializedName("name_course")   var name_course  : String,
        @SerializedName("image_course")  var image_course : String,
        @SerializedName("url_download")  var url_download : String,
)