package com.example.sshmanager.util

import android.content.Context
import android.content.Intent
import com.example.sshmanager.service.SshForegroundService

object ServiceManager {
    fun startSshService(
        context: Context,
        serverName: String,
        serverHost: String
    ) {
        val intent = Intent(context, SshForegroundService::class.java).apply {
            action = "START_TUNNEL"
            putExtra("server_name", serverName)
            putExtra("server_host", serverHost)
        }
        context.startService(intent)
    }

    fun stopSshService(context: Context) {
        val intent = Intent(context, SshForegroundService::class.java).apply {
            action = "STOP_TUNNEL"
        }
        context.startService(intent)
    }

    fun stopServiceForeground(context: Context) {
        context.stopService(Intent(context, SshForegroundService::class.java))
    }
}