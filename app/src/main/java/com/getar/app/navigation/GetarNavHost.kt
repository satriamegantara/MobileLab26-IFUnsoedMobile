package com.getar.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.getar.app.ui.detail.DetailScreen
import com.getar.app.ui.home.HomeScreen
import com.getar.app.ui.home.HomeViewModel

@Composable
fun GetarNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    // ViewModel diinisialisasi di level NavHost agar dibagikan (shared) ke HomeScreen & DetailScreen
    viewModel: HomeViewModel = viewModel()
) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME,
        modifier = modifier
    ) {
        // Layar 1: Home Screen
        composable(route = Routes.HOME) {
            HomeScreen(
                viewModel = viewModel,
                onItemClick = { earthquake ->
                    navController.navigate(Routes.detail(earthquake.id))
                }
            )
        }

        // Layar 2: Detail Screen
        composable(
            route = Routes.DETAIL,
            arguments = listOf(
                navArgument("id") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: -1
            val earthquake = viewModel.findById(id)

            DetailScreen(
                earthquake = earthquake,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
