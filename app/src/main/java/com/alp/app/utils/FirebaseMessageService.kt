/*
 * *
 *  * Created by estiv on 3/07/21 09:56 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 29/06/21 05:05 PM
 *
 */

package com.alp.app.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.alp.app.ui.main.view.activities.HomeActivity
import com.alp.app.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class FirebaseMessageService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val title = remoteMessage.notification!!.title
        val message = remoteMessage.notification!!.body
        sendNotification(title!!,message!!)
    }

    private fun sendNotification(title: String, message: String) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT)
        val canalID = getString(R.string.default_notification_channel_id)
        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, canalID)
            .setSmallIcon(R.drawable.ic_logo_notifications)
            .setContentTitle(title)
            .setContentText(message)
            .setColor(ContextCompat.getColor(applicationContext,R.color.blue_700))
            .setLargeIcon(BitmapFactory.decodeResource(resources,R.drawable.ic_logo_notifications))
            .setAutoCancel(true)
            .setSound(sound)
            .setNumber(1)
            .setContentIntent(pendingIntent)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(canalID, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    override fun onNewToken(p0: String) {
        Log.d("token_refresh", p0)
        super.onNewToken(p0)
    }
}