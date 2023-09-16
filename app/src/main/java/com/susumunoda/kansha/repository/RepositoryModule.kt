package com.susumunoda.kansha.repository

import com.susumunoda.android.auth.AuthController
import com.susumunoda.android.auth.SessionListener
import com.susumunoda.android.auth.SessionListenerHandler
import com.susumunoda.android.firebase.firestore.FirestoreService
import com.susumunoda.android.firebase.firestore.FirestoreServiceImpl
import com.susumunoda.kansha.repository.category.CategoryRepository
import com.susumunoda.kansha.repository.category.FirebaseCategoryRepository
import com.susumunoda.kansha.repository.note.FirebaseNoteRepository
import com.susumunoda.kansha.repository.note.NoteRepository
import com.susumunoda.kansha.repository.user.FirebaseUserRepository
import com.susumunoda.kansha.repository.user.UserRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    companion object {
        @Provides
        fun provideSessionListeners(categoryRepository: FirebaseCategoryRepository): List<SessionListener> =
            listOf(categoryRepository)

        // Singleton to ensure that only one instance is ever reacting to changes to the session,
        // including across configuration changes
        @Singleton
        @Provides
        fun provideSessionListenerHandler(
            listeners: List<@JvmSuppressWildcards SessionListener>,
            authController: AuthController,
            coroutineScope: CoroutineScope
        ) = SessionListenerHandler(listeners, authController, coroutineScope)

        @Provides
        fun provideFirestoreService(): FirestoreService = FirestoreServiceImpl()
    }

    @Binds
    abstract fun bindUserRepository(userRepository: FirebaseUserRepository): UserRepository

    @Binds
    abstract fun bindNoteRepository(noteRepository: FirebaseNoteRepository): NoteRepository

    // Categories will be stored in memory for optimized UX. This is OK because the number of
    // categories will be relatively few, and they change infrequently. The same repository instance
    // must be used across the application in order to take advantage of this shared state.
    @Singleton
    @Binds
    abstract fun bindCategoryRepository(categoryRepository: FirebaseCategoryRepository): CategoryRepository
}