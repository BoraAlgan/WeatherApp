package com.example.myweather.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.myweather.R
import com.example.myweather.ui.main.MainActivity
import kotlin.random.Random


class NotificationUtil {


    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotification(context: Context, channelId: String, channelName: String) {

        val contentIntent = Intent(context, MainActivity::class.java)
        contentIntent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
//        val contentPendingIntent = PendingIntent.getActivity(context,0,contentIntent,PendingIntent.FLAG_IMMUTABLE)


        val builder = NotificationCompat.Builder(context, channelId)
            .setContentTitle("Konum Ekle")
            .setContentText("Konum eklemek için sağ üstteki buttonu kullanın!")
            .setSmallIcon(R.drawable.ic_add_location)
            .setAutoCancel(true)
//            .setContentIntent(contentPendingIntent)

        val notification = builder.build()


        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)

        val notificationId = Random.nextInt()

        notificationManager.notify(notificationId, notification)

    }

}