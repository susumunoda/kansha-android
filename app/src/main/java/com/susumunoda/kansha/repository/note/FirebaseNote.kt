package com.susumunoda.kansha.repository.note

import com.google.firebase.firestore.ServerTimestamp
import java.util.Calendar
import java.util.Date

data class FirebaseNote(
    @ServerTimestamp override val createdAt: Date = Calendar.getInstance().time,
    override val message: String = "",
    override val categoryId: String? = null
) : Note
