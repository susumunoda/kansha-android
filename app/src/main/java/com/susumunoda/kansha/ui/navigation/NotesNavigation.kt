package com.susumunoda.kansha.ui.navigation

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
        composableWithoutTransitions(NotesScreen.VIEW_CATEGORIES.name) {
            ViewCategoriesScreen(navController)
        }

        val categoryIdKey = "categoryId"
        val categoryNameKey = "categoryName"
        composableWithoutTransitions("${NotesScreen.VIEW_CATEGORY.name}/{$categoryIdKey}/{$categoryNameKey}") {
            val arguments = it.arguments!!
            val categoryId = arguments.getString(categoryIdKey)!!
            val categoryName = arguments.getString(categoryNameKey)!!
            ViewCategoryScreen(
                navController = navController,
                categoryId = categoryId,
                categoryName = categoryName
            )
        }

        composableWithoutTransitions(NotesScreen.ADD_CATEGORY.name) {
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