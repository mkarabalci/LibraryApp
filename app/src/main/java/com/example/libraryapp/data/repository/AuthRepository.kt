package com.example.libraryapp.data.repository

import kotlinx.coroutines.delay
import kotlin.random.Random

class AuthRepository
{
    suspend fun signIn(email: String, password: String) : Result<Unit> = runCatching { //runcatching -> try catch mantığını direkt yapar. Resulta göre
        delay(2000) // dışarıya istek atıyormuş gibi gecikme verdik kafadan

        val isSuccess = Random.nextBoolean()
        if (isSuccess)
            Unit
        else
            throw Exception("Fake login failed")
    }

    suspend fun signUp(email: String, password: String) : Result<Unit> = runCatching {
        delay(2000)

        val isSuccess = Random.nextBoolean()
        if (isSuccess)
            Unit
        else
            throw Exception ("Fake signUp failed")
    }
}