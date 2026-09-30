package com.example.sshmanager.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.sshmanager.MainActivity
import com.example.sshmanager.R

class SshForegroundService : Service() {
    companion object {
        const val CHANNEL_ID = "ssh_tunnel_channel"
        const val NOTIFICATION_ID = 1001
    }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getSystemService(NotificationManager::class.java).createNotificationChannel(
                NotificationChannel(
                    CHANNEL_ID,
                    "SSH Tunnel Connection",
                    NotificationManager.IMPORTANCE_LOW
                )
            )
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val connected = intent?.action == "START_TUNNEL"
        val host = intent?.getStringExtra("server_host") ?: "SSH Tunnel"
        val openIntent = PendingIntent.getActivity(
            this, 0, Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("SSH Tunnel Manager")
            .setContentText(if (connected) "Connected to $host" else "Connection idle")
            .setContentIntent(openIntent)
            .setOngoing(connected)
            .build()
        startForeground(NOTIFICATION_ID, notification)
        if (!connected && intent?.action == "STOP_TUNNEL") stopSelf()
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null
}