package com.susumunoda.kansha.data.user

import com.google.firebase.firestore.Exclude

data class UserData(@get:Exclude val id: String? = null, val displayName: String? = null)
