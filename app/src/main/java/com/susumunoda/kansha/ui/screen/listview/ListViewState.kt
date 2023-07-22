package com.susumunoda.kansha.ui.screen.listview

import com.susumunoda.kansha.data.Message
import com.susumunoda.kansha.ui.screen.listview.ListViewViewModel.FilterType

data class ListViewState(
    val isFilterExpanded: Boolean = false,
    val filterType: FilterType = FilterType.ALL,
    val entries: List<Message> = listOf()
)
