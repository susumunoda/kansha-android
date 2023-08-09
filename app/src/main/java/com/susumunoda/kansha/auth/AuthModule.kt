package com.susumunoda.kansha.auth

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
// For now, we can install auth bindings in ActivityComponent because the app uses a single-activity
// architecture. If in the future the app grows to use multiple activities, this should probably
// become SingletonComponent so that there is a single source of truth for auth across the entire app.
@InstallIn(ActivityComponent::class)
abstract class AuthModule {
    // Scope this to the Activity so that all uses of AuthController refer to the same instance.
    // This is important because the application subscribes to changes in the auth state (e.g. login
    // and logout events), and all clients must be observing the same source of information.
    @ActivityScoped
    @Binds
    abstract fun bindAuthController(authController: FirebaseAuthController): AuthController
}