package com.susumunoda.kansha.data.user

import com.google.firebase.firestore.ServerTimestamp
import java.util.Calendar
import java.util.Date

data class User(
    @ServerTimestamp val createdAt: Date = Calendar.getInstance().time,
    val displayName: String = "",
    val profilePhotoUrl: String = "",
    val backgroundPhotoUrl: String = ""
)
