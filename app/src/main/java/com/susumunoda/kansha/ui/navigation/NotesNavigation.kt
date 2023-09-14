package com.susumunoda.kansha.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.susumunoda.compose.animation.enterSlidingUp
import com.susumunoda.compose.animation.exitSlidingDown
import com.susumunoda.kansha.ui.screen.notes.AddCategoryScreen
import com.susumunoda.kansha.ui.screen.notes.AddNoteScreen
import com.susumunoda.kansha.ui.screen.notes.ViewCategoriesScreen
import com.susumunoda.kansha.ui.screen.notes.ViewCategoryScreen

fun NavGraphBuilder.notesNavigation(navController: NavHostController) {
    navigation(
        route = Destination.NOTES.route,
        startDestination = Destination.VIEW_CATEGORIES.route
    ) {
        composableWithoutTransitions(Destination.VIEW_CATEGORIES.route) {
            ViewCategoriesScreen(navController)
        }

        val categoryIdKey = "categoryId"
        val categoryNameKey = "categoryName"
        composableWithoutTransitions("${Destination.VIEW_CATEGORY.route}/{$categoryIdKey}/{$categoryNameKey}") {
            val arguments = it.arguments!!
            val categoryId = arguments.getString(categoryIdKey)!!
            val categoryName = arguments.getString(categoryNameKey)!!
            ViewCategoryScreen(
                navController = navController,
                categoryId = categoryId,
                categoryName = categoryName
            )
        }

        composableWithConditionalTransitions(
            route = Destination.ADD_CATEGORY.route,
            enterTransition = { enterSlidingUp() },
            enterTransitionFrom = Destination.VIEW_CATEGORIES.route,
            exitTransition = { exitSlidingDown() },
            exitTransitionTo = Destination.VIEW_CATEGORIES.route
        ) {
            AddCategoryScreen(navController)
        }

        composableWithConditionalTransitions(
            route = Destination.ADD_NOTE.route,
            enterTransition = { enterSlidingUp() },
            enterTransitionFrom = Destination.VIEW_CATEGORIES.route,
            exitTransition = { exitSlidingDown() },
            exitTransitionTo = Destination.VIEW_CATEGORIES.route
        ) {
            AddNoteScreen(navController = navController)
        }
    }
}

fun categoryDestination(categoryId: String, categoryName: String) =
    "${Destination.VIEW_CATEGORY.route}/$categoryId/$categoryName"