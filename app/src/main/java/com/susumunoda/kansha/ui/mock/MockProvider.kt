package com.susumunoda.kansha.ui.mock

import com.susumunoda.kansha.auth.Session

internal class MockProvider {
    // Session.User
    var sessionUserId: String = "1"
    val sesssionUser
        get() = Session.User(sessionUserId)

    // MockAuthController
    val authController
        get() = MockAuthController(Session(sesssionUser))

    // MockUser
    var userDisplayName: String = "John Smith"
    var userProfilePhotoUrl: String = "profile.jpg"
    var userBackgroundPhotoUrl: String = "background.jpg"
    val user
        get() = MockUser(
            displayName = userDisplayName,
            profilePhotoUrl = userProfilePhotoUrl,
            backgroundPhotoUrl = userBackgroundPhotoUrl
        )

    // MockUserRepository
    var userRepositoryDatabase: MutableMap<String, MockUser> = mutableMapOf()
    var userRepositoryMockLatency: Boolean = false
    var userRepositoryMockLatencyMillis: Long = 1000
    var userRepositoryErrorOnGetUser: Boolean = false
    var userRepositoryErrorOnSetUser: Boolean = false
    val userRepository
        get() = MockUserRepository(
            database = userRepositoryDatabase,
            mockLatency = userRepositoryMockLatency,
            mockLatencyMillis = userRepositoryMockLatencyMillis,
            errorOnGetUser = userRepositoryErrorOnGetUser,
            errorOnSetUser = userRepositoryErrorOnSetUser
        )

    // MockNoteRepository
    var noteRepositoryDatabase: MutableMap<String, MutableList<MockNote>> = mutableMapOf()
    var noteRepositoryMockLatency: Boolean = false
    var noteRepositoryMockLatencyMillis: Long = 1000
    var noteRepositoryErrorOnAddNote: Boolean = false
    var noteRepositoryErrorOnNotesFlow: Boolean = false
    val noteRepository
        get() = MockNoteRepository(
            database = noteRepositoryDatabase,
            mockLatency = noteRepositoryMockLatency,
            mockLatencyMillis = noteRepositoryMockLatencyMillis,
            errorOnAddNote = noteRepositoryErrorOnAddNote,
            errorOnNotesFlow = noteRepositoryErrorOnNotesFlow
        )

    // MockCategoryRepository
    var categoryRepositoryDatabase: MutableMap<String, MutableList<MockCategory>> = mutableMapOf()
    var categoryRepositoryMockLatency: Boolean = false
    var categoryRepositoryMockLatencyMillis: Long = 1000
    var categoryRepositoryErrorOnCategoriesFlow: Boolean = false
    var categoryRepositoryErrorOnAddCategory: Boolean = false
    val categoryRepository
        get() = MockCategoryRepository(
            database = categoryRepositoryDatabase,
            mockLatency = categoryRepositoryMockLatency,
            mockLatencyMillis = categoryRepositoryMockLatencyMillis,
            errorOnCategoriesFlow = categoryRepositoryErrorOnCategoriesFlow,
            errorOnAddCategory = categoryRepositoryErrorOnAddCategory
        )
}