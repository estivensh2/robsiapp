package com.alp.app.servicios

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.alp.app.EntradaActivity
import com.alp.app.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random


class FirebaseMensajesServicio : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val titulo = remoteMessage.notification!!.title
        val mensaje = remoteMessage.notification!!.body
        enviarNotificacion(titulo!!,mensaje!!)
    }

    private fun enviarNotificacion(titulo: String, mensaje: String) {
        val intent = Intent(this, EntradaActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT)
        val canalID = getString(R.string.default_notification_channel_id)
        val sonido = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, canalID)
            .setSmallIcon(R.drawable.notificaciones)
            .setContentTitle(titulo)
            .setContentText(mensaje)
            .setColor(ContextCompat.getColor(applicationContext,R.color.colorAzulClaro))
            .setLargeIcon(BitmapFactory.decodeResource(resources,R.drawable.notificaciones))
            .setAutoCancel(true)
            .setSound(sonido)
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
        super.onNewToken(p0)
    }
}