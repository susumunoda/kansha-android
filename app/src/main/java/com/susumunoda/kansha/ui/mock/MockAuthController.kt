package com.susumunoda.kansha.ui.mock

import com.susumunoda.kansha.auth.AuthController
import com.susumunoda.kansha.auth.Session
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal class MockAuthController(session: Session = Session.LOGGED_OUT) :
    AuthController {
    private val database = mutableMapOf<String, Session.User>()

    init {
        if (session !== Session.LOGGED_OUT) {
            database[session.user.id] = session.user
        }
    }

    private val _sessionFlow = MutableStateFlow(session)
    override val sessionFlow = _sessionFlow.asStateFlow()

    override suspend fun createUser(email: String, password: String): Session.User {
        val user = Session.User("$email+$password")
        database[user.id] = user
        _sessionFlow.update { Session(user) }
        return user
    }

    override suspend fun login(email: String, password: String): Session.User {
        val user = database["$email+$password"]!!
        _sessionFlow.update { Session(user) }
        return user
    }

    override fun logout() {
        _sessionFlow.update { Session.LOGGED_OUT }
    }
}