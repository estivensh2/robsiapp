/*
 * *
 *  * Created by estiven on 3/08/21, 3:05 p. m.
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 16/07/21, 8:04 p. m.
 *
 */

package com.alp.app.data.model

import android.os.Parcel
import android.os.Parcelable


data class OptionsModel(val id_option : Int?, val option: String?) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id_option)
        parcel.writeString(option)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OptionsModel> {
        override fun createFromParcel(parcel: Parcel): OptionsModel {
            return OptionsModel(parcel)
        }

        override fun newArray(size: Int): Array<OptionsModel?> {
            return arrayOfNulls(size)
        }
    }

}