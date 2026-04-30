package com.example.libraryapp.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.libraryapp.ui.viewmodel.AuthViewModel

@Composable
fun HomeScreen() {
    val authViewModel: AuthViewModel = viewModel()
    val profileState = authViewModel.profile.collectAsState()

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(profileState.value?.fullName ?: "Profil bulunamadı")
    }

}