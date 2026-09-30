package com.example.sshmanager.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sshmanager.data.SshServerEntity
import com.example.sshmanager.ui.screens.AddEditServerScreen
import com.example.sshmanager.ui.screens.HomeScreen
import com.example.sshmanager.viewmodel.SshViewModel

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AddServer : Screen("add_server")
    object EditServer : Screen("edit_server")
}

@Composable
fun Navigation(
    navController: NavHostController = rememberNavController(),
    viewModel: SshViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = viewModel,
                onNavigateToAdd = {
                    navController.navigate(Screen.AddServer.route)
                },
                onNavigateToEdit = { server ->
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        "server_to_edit",
                        server
                    )
                    navController.navigate(Screen.EditServer.route)
                }
            )
        }

        composable(Screen.AddServer.route) {
            AddEditServerScreen(
                viewModel = viewModel,
                serverToEdit = null,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.EditServer.route) { backStackEntry ->
            val serverToEdit = backStackEntry.savedStateHandle.get<SshServerEntity>("server_to_edit")
            AddEditServerScreen(
                viewModel = viewModel,
                serverToEdit = serverToEdit,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}