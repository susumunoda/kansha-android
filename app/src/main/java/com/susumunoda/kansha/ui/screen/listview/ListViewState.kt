package com.susumunoda.kansha.ui.screen.listview

import com.susumunoda.kansha.ui.screen.listview.ListViewViewModel.FilterType
import com.susumunoda.kansha.data.Message

data class ListViewState(
    val filterType: FilterType = FilterType.ALL,
    val entries: List<Message> = listOf()
)
