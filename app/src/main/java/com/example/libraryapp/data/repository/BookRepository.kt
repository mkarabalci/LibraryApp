package com.example.libraryapp.data.repository

import com.example.libraryapp.data.model.Book
import com.example.libraryapp.data.supabase.supabase
import io.github.jan.supabase.postgrest.postgrest

class BookRepository {

    //bütün kitapları listeleme
    suspend fun  getAllBooks() : Result<List<Book>> = runCatching {
        supabase.postgrest["books"]
            .select()
            .decodeList<Book>()
    }

    //Id'ye göre isteme
    suspend fun getBookById(id: String): Result<Book> = runCatching {
        supabase.postgrest["books"]
            .select { filter { eq("id", id) } }
            .decodeSingle<Book>()
    }

    //kitap ekleme
    suspend fun addBook(book: Book): Result<Unit> = runCatching {
        if (book.title.length < 3)
            return@runCatching
        supabase.postgrest["books"].insert(book)
    }

    //Güncelleme
    suspend fun updateBook(id: String, book: Book): Result<Unit> = runCatching {
        supabase.postgrest["books"]
            .update({
                set("title", book.title)
                set("author", book.author)
                set("isbn", book.isbn)
                set("category", book.category)
                set("page_count", book.pageCount)
                set("total_copies", book.totalCopies)
                set("available_copies", book.availableCopies)
            }) { filter { eq("id", id) } }
    }

    //Silme
    suspend fun deleteBook(id: String): Result<Unit> = runCatching {
        supabase.postgrest["books"]
            .delete { filter { eq("id", id) } }
    }

    //Arama
    suspend fun searchBook(query: String) : Result<List<Book>> = runCatching {
        supabase.postgrest["books"]
            .select { filter { ilike("title", "%$query%") } }
            .decodeList<Book>()
    }

}

