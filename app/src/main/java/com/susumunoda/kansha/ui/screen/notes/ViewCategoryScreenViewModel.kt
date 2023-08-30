package com.susumunoda.kansha.ui.screen.notes

import androidx.lifecycle.ViewModel
import com.susumunoda.kansha.auth.AuthController
import com.susumunoda.kansha.data.note.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ViewCategoryScreenViewModel @Inject constructor(
    private val authController: AuthController,
    private val noteRepository: NoteRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ViewCategoryScreenState())
    val uiState = _uiState.asStateFlow()

    suspend fun fetchNotes(categoryId: String) {
        val currentUserId = authController.sessionFlow.value.user.id
        when (categoryId) {
            CATEGORY_ALL -> {
                noteRepository.notesFlow(currentUserId)
            }

            CATEGORY_NONE -> {
                noteRepository.notesFlow(currentUserId, null)
            }

            else -> {
                noteRepository.notesFlow(currentUserId, categoryId)
            }
        }.collect { notes ->
            _uiState.update { it.copy(notes = notes) }
        }
    }
}