package com.susumunoda.kansha.ui.screen.notes

import androidx.lifecycle.ViewModel
import com.susumunoda.kansha.auth.AuthController
import com.susumunoda.kansha.data.category.CategoryRepository
import com.susumunoda.kansha.data.note.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ViewCategoryScreenViewModel @Inject constructor(
    private val authController: AuthController,
    private val noteRepository: NoteRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    private val _uiState =
        MutableStateFlow(ViewCategoryScreenState(categoryRepository.newInstance()))
    val uiState = _uiState.asStateFlow()

    suspend fun fetchCategory(categoryId: String) {
        if (categoryId != CATEGORY_ALL && categoryId != CATEGORY_NONE) {
            val currentUserId = authController.sessionFlow.value.user.id
            withContext(Dispatchers.IO) {
                categoryRepository.categoriesFlow(currentUserId)
            }.collect { categories ->
                val category = categories.find { category -> category.id == categoryId }
                if (category != null) {
                    _uiState.update { it.copy(category = category) }
                }
            }
        }
    }

    suspend fun fetchNotes(categoryId: String) {
        val currentUserId = authController.sessionFlow.value.user.id
        withContext(Dispatchers.IO) {
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
            }
        }.collect { notes ->
            _uiState.update { it.copy(notes = notes) }
        }
    }
}