package com.susumunoda.kansha.ui.screen.profile

import com.susumunoda.kansha.data.note.Note
import com.susumunoda.kansha.data.user.User

data class ProfileScreenState(
    val user: User = User(),
    val userFetchInProgress: Boolean = true,
    val userFetchFailed: Boolean = false,

    val notes: List<Note> = emptyList(),
    val notesFetchInProgress: Boolean = true,
    val notesFetchFailed: Boolean = false
)