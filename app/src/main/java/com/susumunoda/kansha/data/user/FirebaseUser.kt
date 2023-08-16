package com.susumunoda.kansha.data.user

import com.google.firebase.firestore.ServerTimestamp
import java.util.Calendar
import java.util.Date

data class FirebaseUser(
    @ServerTimestamp override val createdAt: Date = Calendar.getInstance().time,
    override val displayName: String = "",
    override val profilePhotoUrl: String = "",
    override val backgroundPhotoUrl: String = ""
) : User
