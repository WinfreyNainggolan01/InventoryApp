package com.example.inventoryapp.presentation.navigation

sealed class Screen(val route:String) {
    object Home: Screen("home")
    object Inventory: Screen("inventory")
    object Profile: Screen("profile")
    object AddProduct: Screen("add_product")
    object Detail: Screen("detail/{productId}") {
        fun createRoute(productId: Int) = "detail/$productId"
    }
}