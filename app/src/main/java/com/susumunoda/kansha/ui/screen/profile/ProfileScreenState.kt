package com.susumunoda.kansha.ui.screen.profile

import com.susumunoda.kansha.data.note.Label
import com.susumunoda.kansha.data.note.Note
import com.susumunoda.kansha.data.user.User

data class ProfileScreenState(
    val user: User,
    val userFetchInProgress: Boolean = true,
    val userFetchFailed: Boolean = false,

    val notes: List<Note> = emptyList(),
    val notesFetchInProgress: Boolean = true,

    val allLabels: List<Label> = emptyList(),
    val allLabelsFetchInProgress: Boolean = true,
    val selectedLabels: List<Label> = emptyList()
)