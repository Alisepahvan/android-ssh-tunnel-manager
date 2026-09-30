package com.example.sshmanager

import android.app.Application
import android.util.Log

class SshApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d("SshApp", "Application created")
    }
}