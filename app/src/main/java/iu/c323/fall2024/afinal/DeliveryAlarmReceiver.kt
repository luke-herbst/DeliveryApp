package iu.c323.fall2024.afinal

import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.util.Log

class DeliveryAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Create and show the notification
        val notification = NotificationCompat.Builder(context, "delivery_channel")
            .setSmallIcon(R.drawable.ic_add)
            .setContentTitle("Delivery Completed")
            .setContentText("Your order has been delivered.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        NotificationManagerCompat.from(context).notify(2, notification)
    }
}