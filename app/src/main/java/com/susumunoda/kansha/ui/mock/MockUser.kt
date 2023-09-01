package com.susumunoda.kansha.ui.mock

import com.susumunoda.kansha.repository.user.User
import java.util.Calendar
import java.util.Date

internal data class MockUser(
    override val createdAt: Date = Calendar.getInstance().time,
    override val displayName: String = "",
    override val profilePhotoUrl: String = "",
    override val backgroundPhotoUrl: String = ""
) : User
