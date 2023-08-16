package com.susumunoda.kansha.data.note

import java.util.Calendar
import java.util.Date

data class MockNote(
    override val createdAt: Date = Calendar.getInstance().time,
    override val message: String = "",
    override val labels: List<String> = emptyList()
) : Note
