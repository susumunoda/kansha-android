package com.susumunoda.kansha.ui.screen.profile

import com.susumunoda.kansha.data.note.NoteData
import com.susumunoda.kansha.data.user.UserData

data class ProfileScreenState(
    val userData: UserData = UserData(),
    val userDataFetchInProgress: Boolean = true,
    val userDataFetchFailed: Boolean = false,

    val notesData: List<NoteData> = emptyList(),
    val notesDataFetchInProgress: Boolean = true,
    val notesDataFetchFailed: Boolean = false
)