/*
 * *
 *  * Created by estiven on 3/08/21, 3:05 p. m.
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 1/08/21, 12:01 a. m.
 *
 */

package com.alp.app.data.model

import com.google.gson.annotations.SerializedName

data class CertificateDetailModel(
    @SerializedName("url_download") val url: String
)