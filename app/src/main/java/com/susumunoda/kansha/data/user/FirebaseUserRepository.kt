package com.susumunoda.kansha.data.user

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.susumunoda.kansha.auth.User

object FirebaseUserRepository : UserRepository {
    private val database = Firebase.database

    object Constants {
        const val FIELD = "users"
    }

    private enum class UserInfoFields {
        NICKNAME,
        PROFILE_PHOTO_URL;

        companion object {
            fun isValidField(field: String): Boolean {
                return try {
                    valueOf(field.uppercase())
                    true
                } catch (e: IllegalArgumentException) {
                    false
                }
            }
        }
    }

    override fun getBasicUserInformation(
        user: User,
        onSuccess: (Map<String, String>) -> Unit,
        onError: (Exception?) -> Unit
    ) {
        database.getReference(Constants.FIELD).child(user.id).get()
            .addOnCompleteListener { snapshot ->
                if (snapshot.exception == null) {
                    if (snapshot.result.value == null || snapshot.result.value !is Map<*, *>) {
                        onSuccess(HashMap())
                    } else {
                        val userInfo = HashMap<String, String>()
                        (snapshot.result.value as Map<*, *>).forEach { (k, v) ->
                            if (k is String && UserInfoFields.isValidField(k) && v is String) {
                                userInfo[k] = v
                            }
                        }
                        onSuccess(userInfo)
                    }
                } else {
                    onError(snapshot.exception)
                }
            }
    }
}