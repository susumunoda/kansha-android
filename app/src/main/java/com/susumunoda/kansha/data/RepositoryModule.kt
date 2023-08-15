package com.susumunoda.kansha.data

import com.susumunoda.kansha.data.note.FirebaseNoteRepository
import com.susumunoda.kansha.data.note.NoteRepository
import com.susumunoda.kansha.data.user.FirebaseUserRepository
import com.susumunoda.kansha.data.user.UserRepository
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
}