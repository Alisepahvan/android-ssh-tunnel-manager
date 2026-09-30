package com.example.sshmanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sshmanager.data.SshServerEntity
import com.example.sshmanager.repository.SshServerRepository
import com.example.sshmanager.service.SshTunnelManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SshUiState(
    val servers: List<SshServerEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val connectedServerId: Long? = null,
    val connectionProgress: String = ""
)

class SshViewModel(
    private val repository: SshServerRepository,
    private val tunnelManager: SshTunnelManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(SshUiState())
    val uiState: StateFlow<SshUiState> = _uiState.asStateFlow()

    init {
        loadServers()
    }

    private fun loadServers() {
        viewModelScope.launch {
            repository.getServers().collect { servers ->
                _uiState.value = _uiState.value.copy(servers = servers)
            }
        }
    }

    fun saveServer(server: SshServerEntity) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                repository.saveServer(server)
                _uiState.value = _uiState.value.copy(isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error"
                )
            }
        }
    }

    fun deleteServer(server: SshServerEntity) {
        viewModelScope.launch {
            try {
                repository.deleteServer(server)
                if (_uiState.value.connectedServerId == server.id) {
                    tunnelManager.disconnect()
                    _uiState.value = _uiState.value.copy(connectedServerId = null)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun connectToServer(server: SshServerEntity) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    isLoading = true,
                    connectionProgress = "Connecting to ${server.host}..."
                )

                if (_uiState.value.connectedServerId != null) {
                    tunnelManager.disconnect()
                }

                val connected = tunnelManager.connect(server)

                if (connected) {
                    repository.updateConnectionState(server.id, true)
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        connectedServerId = server.id,
                        connectionProgress = "Connected to ${server.host}",
                        error = null
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Failed to connect to ${server.host}",
                        connectionProgress = ""
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Connection error",
                    connectionProgress = ""
                )
            }
        }
    }

    fun disconnectFromServer() {
        viewModelScope.launch {
            try {
                tunnelManager.disconnect()
                _uiState.value.connectedServerId?.let {
                    repository.updateConnectionState(it, false)
                }
                _uiState.value = _uiState.value.copy(
                    connectedServerId = null,
                    connectionProgress = "Disconnected"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}