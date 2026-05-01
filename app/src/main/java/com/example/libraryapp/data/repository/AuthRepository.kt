package com.example.libraryapp.data.repository

import com.example.libraryapp.data.model.Profile
import com.example.libraryapp.data.supabase.supabase
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest


class AuthRepository
{
    suspend fun signIn(email: String, password: String) : Result<Unit> = runCatching { //runcatching -> try catch mantığını direkt yapar. Resulta göre
        supabase.auth.signInWith(Email){
           this.email = email
           this.password = password
        }
    }

    suspend fun signUp(
        email: String,
        password: String,
        fullName: String,
        studentNo: String?
    ) : Result<Unit> = runCatching {
        supabase.auth.signUpWith(Email){
            this.email = email
            this.password = password
        }

        val userId = supabase.auth.currentUserOrNull()?.id ?: error("Kullanıcı bulunamadı")

        supabase.postgrest["profiles"].insert(
            Profile(userId, "student", fullName, studentNo)
        )

        // Kayıt sonrası otomatik giriş
        supabase.auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
    }

    fun getCurrentUserId() : String? //o anki user Id sini geri dönen fonksiyon
    {
        return supabase.auth.currentUserOrNull()?.id
    }

    suspend fun  getProfile(userId: String): Profile? =runCatching {
        supabase.postgrest["profiles"]
            .select { filter { eq("user_id", userId) } }
            .decodeSingle<Profile>()
    }.getOrNull()
}