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

private const val CATEGORY_ID_KEY = "categoryId"
private const val CATEGORY_NAME_KEY = "categoryName"

fun NavGraphBuilder.notesNavigation(navController: NavHostController) {
    navigation(
        route = Destination.NOTES.route,
        startDestination = Destination.VIEW_CATEGORIES.route
    ) {
        composableWithoutTransitions(Destination.VIEW_CATEGORIES.route) {
            ViewCategoriesScreen(navController)
        }

        composableWithoutTransitions("${Destination.VIEW_CATEGORY.route}/{$CATEGORY_ID_KEY}/{$CATEGORY_NAME_KEY}") {
            val arguments = it.arguments!!
            val categoryId = arguments.getString(CATEGORY_ID_KEY)!!
            val categoryName = arguments.getString(CATEGORY_NAME_KEY)!!
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
            route = "${Destination.ADD_NOTE.route}/{$CATEGORY_ID_KEY}",
            enterTransition = { enterSlidingUp() },
            enterTransitionFrom = Destination.VIEW_CATEGORIES.route,
            exitTransition = { exitSlidingDown() },
            exitTransitionTo = Destination.VIEW_CATEGORIES.route
        ) {
            val arguments = it.arguments!!
            val categoryId = arguments.getString(CATEGORY_ID_KEY)!!
            AddNoteScreen(navController = navController, categoryId = categoryId)
        }
    }
}

fun categoryDestination(categoryId: String, categoryName: String) =
    "${Destination.VIEW_CATEGORY.route}/$categoryId/$categoryName"

fun addNoteDestination(categoryId: String) = "${Destination.ADD_NOTE.route}/$categoryId"
