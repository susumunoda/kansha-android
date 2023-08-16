package com.susumunoda.kansha.ui.animation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry

private const val SLIDE_DURATION_MILLIS = 350

fun AnimatedContentTransitionScope<NavBackStackEntry>.enterSlidingUp(durationMillis: Int = SLIDE_DURATION_MILLIS) =
    slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Up,
        animationSpec = tween(
            durationMillis = durationMillis,
            easing = FastOutSlowInEasing
        )
    )

fun AnimatedContentTransitionScope<NavBackStackEntry>.exitSlidingDown(durationMillis: Int = SLIDE_DURATION_MILLIS) =
    slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Down,
        animationSpec = tween(
            durationMillis = durationMillis,
            easing = FastOutSlowInEasing
        )
    )
