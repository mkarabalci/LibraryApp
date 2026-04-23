package com.example.libraryapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.libraryapp.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


//Sistem bu 4ünden birinde olabilir
sealed class AuthState {
    object Idle : AuthState() //sayfa açık dururken
    object Loading : AuthState() //sayfa yüklenirken
    data class Success(val role: String) : AuthState() //
    data class  Error(val message: String) : AuthState()
}



class AuthViewModel : ViewModel()
{
    private val repository = AuthRepository()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun signIn(email: String, password: String)
    {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            repository
                .signIn(email,password)
                .onSuccess { result -> _authState.value = AuthState.Success("student") }
                .onFailure { ex -> _authState.value = AuthState.Error(ex.message ?: "Giriş başarısız") }
        }

    }


    fun signUp(email: String, password: String)
    {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            repository
                .signUp(email,password)
                .onSuccess { result -> _authState.value = AuthState.Success("person") }
                .onFailure { ex -> _authState.value = AuthState.Error(ex.message ?: "Kayıt başarısız") }
        }

    }
}