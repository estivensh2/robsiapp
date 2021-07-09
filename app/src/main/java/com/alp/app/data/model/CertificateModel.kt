/*
 * *
 *  * Created by estiv on 3/07/21 09:56 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 8/06/21 05:33 PM
 *
 */

package com.alp.app.data.model

import com.google.gson.annotations.SerializedName

class CertificateModel (
        @SerializedName("name_course")   var name_course   : String,
        @SerializedName("image_course")  var image_course  : String,
        @SerializedName("url_download")  var url_download  : String,
)