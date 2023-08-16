package com.susumunoda.kansha.data.user

import java.util.Calendar
import java.util.Date

data class MockUser(
    override val createdAt: Date = Calendar.getInstance().time,
    override val displayName: String = "",
    override val profilePhotoUrl: String = "",
    override val backgroundPhotoUrl: String = ""
) : User
