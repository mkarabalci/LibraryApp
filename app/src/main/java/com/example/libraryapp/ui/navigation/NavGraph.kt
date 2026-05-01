package com.example.libraryapp.ui.navigation


import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.libraryapp.ui.screen.HomeScreen
import com.example.libraryapp.ui.screen.LoginScreen
import com.example.libraryapp.ui.screen.SignUpScreen
import com.example.libraryapp.ui.viewmodel.AuthViewModel
import com.example.libraryapp.ui.viewmodel.BookViewModel


@Composable
fun NavGraph (navController: NavHostController = rememberNavController())
{
    val authViewModel: AuthViewModel = viewModel()
    val bookViewModel: BookViewModel = viewModel()

    NavHost(navController = navController, startDestination = Screen.Login.route)
    {
        composable(Screen.Login.route) { LoginScreen(
            onNavigateToRegister = { navController.navigate(Screen.Register.route) },
            onLoginSuccess = {role ->
                navController.navigate(Screen.HomePage.route) {
                    popUpTo(Screen.Login.route) {inclusive=true} //login başarılı olduğunda yığını öldürmek için kullanılır. yığın temizlenmiş olur
                    //eğer inclusive=false olsaydı yığın yalnızca verilen URL ile kalacaktı
                }},
            authViewModel
        ) }
        composable (Screen.Register.route) { SignUpScreen(
            onNavigateToLogin = { navController.navigate(Screen.Login.route) },
            onNavigateToHome = {
                navController.navigate(Screen.HomePage.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            },
            authViewModel
        ) }
        composable(Screen.HomePage.route) {
            HomeScreen(authViewModel, bookViewModel)
        }
    }
}