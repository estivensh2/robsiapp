/*
 * *
 *  * Created by estiv on 31/07/21, 10:47 p. m.
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 31/07/21, 10:47 p. m.
 *
 */

package com.alp.app.data.model

import com.google.gson.annotations.SerializedName

data class CertificateDetailModel(
    @SerializedName("url_download") val url: String
)