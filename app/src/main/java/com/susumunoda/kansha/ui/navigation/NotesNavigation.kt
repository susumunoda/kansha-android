package com.susumunoda.kansha.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
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

fun NavGraphBuilder.notesNavigation(navController: NavHostController) {
    navigation(
        route = Destination.NOTES.name,
        startDestination = NotesScreen.VIEW_CATEGORIES.name
    ) {
        composable(
            route = NotesScreen.VIEW_CATEGORIES.name,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            ViewCategoriesScreen(navController)
        }
        composable(
            route = "${NotesScreen.VIEW_CATEGORY.name}/{categoryId}/{categoryName}",
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            val arguments = it.arguments!!
            val categoryId = arguments.getString("categoryId")!!
            val categoryName = arguments.getString("categoryName")!!
            ViewCategoryScreen(
                navController = navController,
                categoryId = categoryId,
                categoryName = categoryName
            )
        }
        composable(
            route = NotesScreen.ADD_CATEGORY.name,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
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