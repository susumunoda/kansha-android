package com.susumunoda.kansha.repository.user

import java.util.Date

interface User {
    val createdAt: Date
    val displayName: String
    val profilePhotoUrl: String
    val backgroundPhotoUrl: String
}
