package com.susumunoda.kansha.data.user

import java.util.Date

interface User {
    val createdAt: Date
    val displayName: String
    val profilePhotoUrl: String
    val backgroundPhotoUrl: String
}
