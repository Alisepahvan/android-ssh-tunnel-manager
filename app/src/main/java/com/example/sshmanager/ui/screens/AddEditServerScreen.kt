package com.example.sshmanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.sshmanager.data.SshServerEntity
import com.example.sshmanager.viewmodel.SshViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditServerScreen(
    viewModel: SshViewModel,
    serverToEdit: SshServerEntity? = null,
    onNavigateBack: () -> Unit
) {
    var name by remember { mutableStateOf(serverToEdit?.name ?: "") }
    var host by remember { mutableStateOf(serverToEdit?.host ?: "") }
    var port by remember { mutableStateOf(serverToEdit?.port?.toString() ?: "22") }
    var username by remember { mutableStateOf(serverToEdit?.username ?: "") }
    var password by remember { mutableStateOf(serverToEdit?.password ?: "") }
    var privateKey by remember { mutableStateOf(serverToEdit?.privateKey ?: "") }
    var localPort by remember { mutableStateOf(serverToEdit?.localPort?.toString() ?: "1080") }
    var remoteHost by remember { mutableStateOf(serverToEdit?.remoteHost ?: "127.0.0.1") }
    var remotePort by remember { mutableStateOf(serverToEdit?.remotePort?.toString() ?: "80") }
    var authType by remember { mutableStateOf(if (serverToEdit?.privateKey != null) "key" else "password") }
    var showPassword by remember { mutableStateOf(false) }
    var isSaving by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (serverToEdit != null) "Edit Server" else "Add Server") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Server Name *") },
                modifier = Modifier.fillMaxWidth(),
                isError = name.isBlank()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = host,
                onValueChange = { host = it },
                label = { Text("Host/IP *") },
                modifier = Modifier.fillMaxWidth(),
                isError = host.isBlank()
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = port,
                    onValueChange = { port = it },
                    label = { Text("Port") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username *") },
                    modifier = Modifier.weight(1f),
                    isError = username.isBlank()
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Authentication Method",
                style = MaterialTheme.typography.labelMedium
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = authType == "password",
                    onClick = { authType = "password" },
                    label = { Text("Password") }
                )
                FilterChip(
                    selected = authType == "key",
                    onClick = { authType = "key" },
                    label = { Text("Private Key") }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            if (authType == "password") {
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password *") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Text(if (showPassword) "Hide" else "Show")
                        }
                    },
                    isError = password.isBlank()
                )
            } else {
                OutlinedTextField(
                    value = privateKey,
                    onValueChange = { privateKey = it },
                    label = { Text("Private Key *") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 100.dp),
                    isError = privateKey.isBlank()
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Port Forwarding",
                style = MaterialTheme.typography.labelMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = localPort,
                    onValueChange = { localPort = it },
                    label = { Text("Local Port") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Text("→", modifier = Modifier.padding(top = 16.dp))
                OutlinedTextField(
                    value = remotePort,
                    onValueChange = { remotePort = it },
                    label = { Text("Remote Port") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = remoteHost,
                onValueChange = { remoteHost = it },
                label = { Text("Remote Host") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (name.isNotBlank() && host.isNotBlank() && username.isNotBlank()) {
                        isSaving = true
                        val server = SshServerEntity(
                            id = serverToEdit?.id ?: 0,
                            name = name,
                            host = host,
                            port = port.toIntOrNull() ?: 22,
                            username = username,
                            password = if (authType == "password") password else null,
                            privateKey = if (authType == "key") privateKey else null,
                            localPort = localPort.toIntOrNull() ?: 1080,
                            remoteHost = remoteHost.ifBlank { "127.0.0.1" },
                            remotePort = remotePort.toIntOrNull() ?: 80
                        )
                        viewModel.saveServer(server)
                        onNavigateBack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                enabled = !isSaving && name.isNotBlank() && host.isNotBlank() && username.isNotBlank()
            ) {
                Text(if (serverToEdit != null) "Update Server" else "Add Server")
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = onNavigateBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Cancel")
            }
        }
    }
}