package com.susumunoda.kansha.repository

import com.susumunoda.kansha.repository.category.CategoryRepository
import com.susumunoda.kansha.repository.category.FirebaseCategoryRepository
import com.susumunoda.kansha.repository.note.FirebaseNoteRepository
import com.susumunoda.kansha.repository.note.NoteRepository
import com.susumunoda.kansha.repository.user.FirebaseUserRepository
import com.susumunoda.kansha.repository.user.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
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