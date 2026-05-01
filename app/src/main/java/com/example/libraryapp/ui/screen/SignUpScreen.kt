package com.example.libraryapp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.libraryapp.ui.viewmodel.AuthState
import com.example.libraryapp.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun SignUpScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit,
    authViewModel: AuthViewModel)
{

    val authState by authViewModel.authState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var studentNo by remember { mutableStateOf("") }

    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            delay(3000)
            authViewModel.resetState()
            onNavigateToHome() // login yerine direkt home'a
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Text(text = "Kayıt Ol",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(30.dp))

        OutlinedTextField(
            enabled = authState !is AuthState.Loading, //loading durumunda işlem yapamazsın şifreye
            modifier = Modifier.fillMaxWidth(),
            value = fullName,
            label = { Text("Ad Soyad")},
            onValueChange = { fullName = it},
            singleLine = true,
        )
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            enabled = authState !is AuthState.Loading,
            modifier = Modifier.fillMaxWidth(),
            value = email,
            label = {Text("E-posta")},
            onValueChange = {email = it},
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            enabled = authState !is AuthState.Loading, //loading durumunda işlem yapamazsın şifreye
            modifier = Modifier.fillMaxWidth(),
            value = password,
            label = { Text("Şifre")},
            onValueChange = {password = it},
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation() //şifreyi gizler
        )
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            enabled = authState !is AuthState.Loading,
            modifier = Modifier.fillMaxWidth(),
            value = studentNo,
            label = {Text("Öğrenci No (opsiyonel)")},
            onValueChange = {studentNo = it},
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { authViewModel.signUp(email, password, fullName, studentNo) },
            modifier = Modifier.fillMaxWidth(),
            enabled = authState !is AuthState.Loading
        ) {
            if (authState is AuthState.Loading)
                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp, color = MaterialTheme.colorScheme.onPrimary)
            else
                Text("Kayıt Ol")
        }


        if (authState is AuthState.Success) {
            Text("Kayıt Olundu")
            Spacer(modifier = Modifier.height(8.dp))
            CircularProgressIndicator(modifier = Modifier.size(24.dp),
                strokeWidth = 2.dp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Giriş sayfasına yönlendiriliyorsunuz")
        }
        else if (authState is AuthState.Error)
            Text(text = (authState as AuthState.Error).message, color = MaterialTheme.colorScheme.error)

        Spacer(modifier = Modifier.height(12.dp))
        TextButton(onClick = { onNavigateToLogin() }) {
            Text("Zaten hesabın var mı? Giriş Yap")
        }
    }
}
