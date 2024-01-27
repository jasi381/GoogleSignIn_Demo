package com.jasmeet.googlesignindemo

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun MainNavigation(navHostController: NavHostController) {

    NavHost(
        navController = navHostController,
        startDestination = Screens.Login.route
    ) {
        composable(Screens.Login.route) {
            LoginScreen(navHostController = navHostController)
        }
        composable(
            Screens.Home.route,
            arguments = listOf(
                navArgument(IMG_URL){
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                },
                navArgument(NAME){
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                },
                navArgument(EMAIL){
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                },
            )
        ) {
            HomeScreen(
                navHostController = navHostController,
                imgUrl = it.arguments?.getString(IMG_URL),
                name = it.arguments?.getString(NAME),
                email = it.arguments?.getString(EMAIL),
                )
        }
    }

}