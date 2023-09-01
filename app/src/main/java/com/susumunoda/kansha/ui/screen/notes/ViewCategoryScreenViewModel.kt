package com.susumunoda.kansha.ui.screen.notes

import androidx.lifecycle.ViewModel
import com.susumunoda.kansha.auth.AuthController
import com.susumunoda.kansha.repository.category.Category
import com.susumunoda.kansha.repository.category.CategoryRepository
import com.susumunoda.kansha.repository.note.NoteRepository
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
        MutableStateFlow(ViewCategoryScreenState())
    val uiState = _uiState.asStateFlow()

    private lateinit var _category: Category

    fun getCategory(categoryId: String): Category? {
        if (this::_category.isInitialized) {
            return _category
        }

        if (categoryId != CATEGORY_ALL && categoryId != CATEGORY_NONE) {
            val categories = categoryRepository.categoriesStateFlow.value
            val category = categories.find { it.id == categoryId }
            if (category != null) {
                _category = category
                return _category
            }
        }

        return null
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