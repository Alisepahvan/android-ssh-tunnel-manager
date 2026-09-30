package com.example.sshmanager.di

import android.content.Context
import com.example.sshmanager.data.AppDatabase
import com.example.sshmanager.repository.SshServerRepository
import com.example.sshmanager.service.SshTunnelManager
import com.example.sshmanager.viewmodel.SshViewModel

object AppModule {
    private var database: AppDatabase? = null
    private var repository: SshServerRepository? = null
    private var tunnelManager: SshTunnelManager? = null

    fun getDatabase(context: Context): AppDatabase {
        return database ?: AppDatabase.getInstance(context).also {
            database = it
        }
    }

    fun getRepository(context: Context): SshServerRepository {
        return repository ?: SshServerRepository(
            getDatabase(context).sshServerDao()
        ).also {
            repository = it
        }
    }

    fun getTunnelManager(): SshTunnelManager {
        return tunnelManager ?: SshTunnelManager().also {
            tunnelManager = it
        }
    }

    fun getViewModel(context: Context): SshViewModel {
        return SshViewModel(
            getRepository(context),
            getTunnelManager()
        )
    }
}