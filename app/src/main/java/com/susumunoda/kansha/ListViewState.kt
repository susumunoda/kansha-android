package com.susumunoda.kansha

import com.susumunoda.kansha.ListViewViewModel.FilterType
import com.susumunoda.kansha.data.Message

data class ListViewState(
    val filterType: FilterType = FilterType.ALL,
    val entries: List<Message> = listOf()
)
