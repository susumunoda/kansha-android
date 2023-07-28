package com.susumunoda.kansha.ui.screen.newmessage

import com.susumunoda.kansha.data.DataSource
import com.susumunoda.kansha.data.User

data class NewMessageState(
    val recipient: User = User.NONE,
    val message: String = "",
    val searchTerm: String = "",
    val searchResults: List<User> = DataSource.allUsers()
)
