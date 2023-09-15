package com.susumunoda.kansha.ui.screen.settings

import androidx.lifecycle.ViewModel
import com.susumunoda.android.auth.AuthController
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val authController: AuthController
) : ViewModel() {
    fun logout() {
        authController.logout()
    }
}