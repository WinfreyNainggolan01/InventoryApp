package com.example.inventoryapp.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.inventoryapp.presentation.components.BottomNavigationBar
import com.example.inventoryapp.presentation.screens.addproduct.AddProductScreen
import com.example.inventoryapp.presentation.screens.detail.DetailScreen
import com.example.inventoryapp.presentation.screens.home.HomeScreen
import com.example.inventoryapp.presentation.screens.inventory.InventoryScreen
import com.example.inventoryapp.presentation.screens.profile.ProfileScreen

@Composable
fun MainNavigation(){
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute != Screen.AddProduct.route
            && currentRoute?.startsWith("detail") == false

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route){
                HomeScreen()
            }

            composable(Screen.Inventory.route) {
                InventoryScreen(
                    onNavigateToDetail = { productId ->
                        navController.navigate(Screen.Detail.createRoute(productId))
                    },
                    onNavigateToAddProduct = {
                        navController.navigate(Screen.AddProduct.route)
                    }
                )
            }
            composable(Screen.Profile.route) {
                ProfileScreen()
            }

            composable(Screen.AddProduct.route) {
                AddProductScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(
                route = Screen.Detail.route,
                arguments = listOf(
                    navArgument("productId") { type = NavType.IntType}
                )
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getInt("productId") ?: 0
                DetailScreen(
                    productId = productId,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}