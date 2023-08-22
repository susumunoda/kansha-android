package com.susumunoda.kansha.ui.animation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry

private const val SLIDE_DURATION_MILLIS = 350

fun AnimatedContentTransitionScope<NavBackStackEntry>.enterSlidingUp(
    durationMillis: Int = SLIDE_DURATION_MILLIS,
    targetOffset: (Int) -> Int = { it }
) =
    slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Up,
        animationSpec = tween(
            durationMillis = durationMillis,
            easing = FastOutSlowInEasing
        ),
        targetOffset
    )

fun AnimatedContentTransitionScope<NavBackStackEntry>.exitSlidingDown(
    durationMillis: Int = SLIDE_DURATION_MILLIS,
    targetOffset: (Int) -> Int = { it }
) =
    slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Down,
        animationSpec = tween(
            durationMillis = durationMillis,
            easing = FastOutSlowInEasing
        ),
        targetOffset
    )

fun AnimatedContentTransitionScope<NavBackStackEntry>.enterSlidingLeft(
    durationMillis: Int = SLIDE_DURATION_MILLIS,
    targetOffset: (Int) -> Int = { it }
) =
    slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Left,
        animationSpec = tween(
            durationMillis = durationMillis,
            easing = FastOutSlowInEasing
        ),
        targetOffset
    )

fun AnimatedContentTransitionScope<NavBackStackEntry>.exitSlidingRight(
    durationMillis: Int = SLIDE_DURATION_MILLIS,
    targetOffset: (Int) -> Int = { it }
) =
    slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(
            durationMillis = durationMillis,
            easing = FastOutSlowInEasing
        ),
        targetOffset
    )
