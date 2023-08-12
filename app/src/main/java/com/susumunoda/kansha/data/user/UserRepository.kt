package com.susumunoda.kansha.data.user


interface UserRepository {
    suspend fun getUserData(id: String): UserData
    suspend fun saveUserData(id: String, userData: UserData)
}