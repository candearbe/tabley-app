package com.uadeapps.tabley.core.navigation


import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.uadeapps.tabley.presentation.notebooks.NotebooksScreen
import com.uadeapps.tabley.presentation.notebook_detail.NotebookDetailScreen
import com.uadeapps.tabley.presentation.table_detail.TableDetailScreen
@Composable
fun TableyNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "notebooks"
    ) {

        composable("notebooks") {
            NotebooksScreen(
                onNotebookClick = { notebookId, notebookName ->
                    navController.navigate(
                        "notebook/$notebookId/${Uri.encode(notebookName)}"
                    ) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = "notebook/{notebookId}/{notebookName}",
            arguments = listOf(
                navArgument("notebookId") {
                    type = NavType.LongType
                },
                navArgument("notebookName") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val notebookId =
                backStackEntry.arguments?.getLong("notebookId") ?: 0L

            val notebookName =
                Uri.decode(
                    backStackEntry.arguments?.getString("notebookName") ?: ""
                )

            NotebookDetailScreen(
                notebookId = notebookId,
                notebookName = notebookName,
                onBackClick = {
                    navController.popBackStack()
                },
                onTableClick = { tableId, tableName ->
                    navController.navigate(
                        "table/$tableId/${Uri.encode(tableName)}"
                    )
                }
            )
        }

        composable(
            route = "table/{tableId}/{tableName}",
            arguments = listOf(
                navArgument("tableId") {
                    type = NavType.LongType
                },
                navArgument("tableName") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val tableId =
                backStackEntry.arguments?.getLong("tableId") ?: 0L

            val tableName =
                Uri.decode(
                    backStackEntry.arguments?.getString("tableName") ?: ""
                )

            TableDetailScreen(
                tableId = tableId,
                tableName = tableName,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
