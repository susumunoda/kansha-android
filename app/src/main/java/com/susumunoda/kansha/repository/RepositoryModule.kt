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

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindUserRepository(userRepository: FirebaseUserRepository): UserRepository

    @Binds
    abstract fun bindNoteRepository(noteRepository: FirebaseNoteRepository): NoteRepository

    @Binds
    abstract fun bindCategoryRepository(categoryRepository: FirebaseCategoryRepository): CategoryRepository
}