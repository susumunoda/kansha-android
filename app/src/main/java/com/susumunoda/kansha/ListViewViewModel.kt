package com.susumunoda.kansha

import androidx.lifecycle.ViewModel
import com.susumunoda.kansha.data.DataSource
import com.susumunoda.kansha.data.filterByRecipient
import com.susumunoda.kansha.data.filterBySender
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ListViewViewModel : ViewModel() {
    private var _uiState = MutableStateFlow(ListViewState(entries = DataSource.allMessages()))
    val uiState = _uiState.asStateFlow()
    private val currentUser = "Bob" // TODO: Implement proper auth

    enum class FilterType(val label: String) {
        ALL("All"),
        FROM_ME("From me"),
        TO_ME("To me"),
    }

    fun setFilter(filterType: FilterType) {
        _uiState.update { current ->
            current.copy(
                filterType = filterType,
                entries = when (filterType) {
                    FilterType.FROM_ME -> DataSource.allMessages().filterBySender(currentUser)
                    FilterType.TO_ME -> DataSource.allMessages().filterByRecipient(currentUser)
                    else -> DataSource.allMessages()
                }
            )
        }
    }
}