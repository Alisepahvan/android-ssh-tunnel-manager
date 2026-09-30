package com.example.sshmanager.service

import android.app.Notification
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
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        val serverName = intent?.getStringExtra("server_name") ?: "SSH Tunnel"
        val serverHost = intent?.getStringExtra("server_host") ?: "Unknown"

        when (action) {
            "START_TUNNEL" -> {
                startForegroundNotification(serverName, serverHost, true)
            }
            "STOP_TUNNEL" -> {
                startForegroundNotification(serverName, serverHost, false)
            }
            else -> {
                startForegroundNotification("SSH Tunnel Manager", "Idle", false)
            }
        }

        return START_STICKY
    }

    private fun startForegroundNotification(
        serverName: String,
        serverHost: String,
        isConnected: Boolean
    ) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(serverName)
            .setContentText(
                if (isConnected) "Connected to $serverHost" else "Connection Idle"
            )
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setOngoing(isConnected)
            .setAutoCancel(!isConnected)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        startForeground(NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "SSH Tunnel Connection",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Shows SSH tunnel connection status"
                enableVibration(true)
            }

            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(STOP_FOREGROUND_REMOVE)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}