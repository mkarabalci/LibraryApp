package com.example.libraryapp.ui.navigation


// Sayfa routelarının tanımı

sealed class Screen (val route: String )
{
    object Login : Screen("login")
    object Register : Screen("register")

    object HomePage : Screen("homepage")

    object Splash : Screen("splash")

    object Borrows : Screen("borrows")
}