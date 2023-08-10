package com.susumunoda.kansha.auth

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {
    // Scope this as a singleton so that all uses of AuthController refer to the same instance.
    // This is important because the application subscribes to changes in the auth state (e.g.
    // login and logout events), and all clients must be observing the same source of information.
    @Singleton
    @Binds
    abstract fun bindAuthController(authController: FirebaseAuthController): AuthController
}