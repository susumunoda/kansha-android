package com.susumunoda.kansha.repository.note

import java.util.Date

interface Note {
    val createdAt: Date
    val message: String
    val categoryId: String?
}
