package com.susumunoda.kansha.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.susumunoda.kansha.ui.animation.enterSlidingUp
import com.susumunoda.kansha.ui.animation.exitSlidingDown
import com.susumunoda.kansha.ui.screen.notes.AddNoteScreen
import com.susumunoda.kansha.ui.screen.notes.ViewCategoriesScreen
import com.susumunoda.kansha.ui.screen.notes.ViewCategoryScreen

enum class NotesScreen {
    VIEW_CATEGORIES,
    VIEW_CATEGORY,
    ADD_CATEGORY,
    ADD_NOTE
}

@Composable
fun NotesNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = NotesScreen.VIEW_CATEGORIES.name) {
        composable(NotesScreen.VIEW_CATEGORIES.name) {
            ViewCategoriesScreen(navController)
        }
        composable("${NotesScreen.VIEW_CATEGORY.name}/{categoryId}/{categoryName}") {
            val arguments = it.arguments!!
            val categoryId = arguments.getString("categoryId")!!
            val categoryName = arguments.getString("categoryName")!!
            ViewCategoryScreen(
                navController = navController,
                categoryId = categoryId,
                categoryName = categoryName
            )
        }
        composable(NotesScreen.ADD_CATEGORY.name) {
//            AddCategoryScreen()
        }
        composable(
            route = NotesScreen.ADD_NOTE.name,
            enterTransition = { enterSlidingUp() },
            exitTransition = { exitSlidingDown() }
        ) {
            AddNoteScreen(navController = navController)
        }
    }
}

fun categoryDestination(categoryId: String, categoryName: String) =
    "${NotesScreen.VIEW_CATEGORY.name}/$categoryId/$categoryName"