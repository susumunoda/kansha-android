package com.susumunoda.kansha.ui.screen.profile

import com.susumunoda.kansha.data.note.NoteData
import com.susumunoda.kansha.data.user.User

data class ProfileScreenState(
    val user: User = User(),
    val userFetchInProgress: Boolean = true,
    val userFetchFailed: Boolean = false,

    val notesData: List<NoteData> = emptyList(),
    val notesDataFetchInProgress: Boolean = true,
    val notesDataFetchFailed: Boolean = false
)