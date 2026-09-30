package com.example.sshmanager.util

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.example.sshmanager.service.SshForegroundService

object ServiceManager {
    fun start(context: Context, host: String) {
        val intent = Intent(context, SshForegroundService::class.java).apply {
            action = "START_TUNNEL"
            putExtra("server_host", host)
        }
        ContextCompat.startForegroundService(context, intent)
    }

    fun stop(context: Context) {
        context.startService(Intent(context, SshForegroundService::class.java).apply {
            action = "STOP_TUNNEL"
        })
    }
}