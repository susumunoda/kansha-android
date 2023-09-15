package com.susumunoda.kansha.auth

import com.susumunoda.android.auth.AuthController
import com.susumunoda.android.firebase.auth.FirebaseAuthController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    // Scope this as a singleton so that all uses of AuthController refer to the same instance.
    // This is important because the application subscribes to changes in the auth state (e.g.
    // login and logout events), and all clients must be observing the same source of information.
    @Singleton
    @Provides
    fun provideAuthController(): AuthController {
        return FirebaseAuthController()
    }
}