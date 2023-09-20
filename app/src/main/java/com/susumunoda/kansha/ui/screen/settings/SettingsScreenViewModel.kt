package com.susumunoda.kansha.ui.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.susumunoda.auth.AuthController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val authController: AuthController
) : ViewModel() {
    fun logout() {
        viewModelScope.launch { authController.logout() }
    }
}