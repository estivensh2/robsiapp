package com.alp.app.servicios

import android.app.Application
import com.google.android.gms.ads.MobileAds

class Appapp : Application() {
    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this)
    }
}