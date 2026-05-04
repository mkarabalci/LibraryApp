package com.example.libraryapp.data.repository

import com.example.libraryapp.data.model.Book
import com.example.libraryapp.data.model.BorrowRecord
import com.example.libraryapp.data.supabase.supabase
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import io.github.jan.supabase.postgrest.query.Columns

class BorrowRepository {

    suspend fun addBorrowRecord(studentId: String, bookId: String): Result<Unit> = runCatching {
        val now = Clock.System.now()
        val dueDate = now.plus(5, DateTimeUnit.DAY, TimeZone.currentSystemDefault())

        supabase.postgrest["borrow_records"].insert(
            BorrowRecord(
                studentId = studentId,
                bookId = bookId,
                dueDate = dueDate.toString()
            )
        )

        // önce kitabı çek
        val book = supabase.postgrest["books"]
            .select { filter { eq("id", bookId) } }
            .decodeSingle<Book>()


        // sonra available_copies azalt
        supabase.postgrest["books"]
            .update({
                set("available_copies", book.availableCopies - 1)
            }) {
                filter { eq("id", bookId) }
        }

    }

    suspend fun hasActiveBorrow(studentId: String, bookId: String): Boolean = runCatching {
        supabase.postgrest["borrow_records"]
            .select {
                filter {
                    eq("student_id", studentId)
                    eq("book_id", bookId)
                    exact("returned_at", null)
                }
            }
            .decodeList<BorrowRecord>()

            .isNotEmpty()
    }.getOrDefault(false)



    // kullanıcının kiralamalarını çeken fonksiyon
    suspend fun getBorrowRecords(studentId: String): Result<List<BorrowRecord>> = runCatching {
        supabase.postgrest["borrow_records"]
            .select(columns = Columns.raw("*, books(title, author)"))
            { filter { eq("student_id", studentId) } }
            .decodeList()
    }

    suspend fun returnBook(borrowId: String): Result<Unit> = runCatching {
        val record = supabase.postgrest["borrow_records"]
            .select { filter { eq("id", borrowId) } }
            .decodeSingle<BorrowRecord>()

        // returned_at güncelle
        supabase.postgrest["borrow_records"]
            .update({
                set("returned_at", Clock.System.now().toString())
            }) {
                filter { eq("id", borrowId) }
            }

        // kitabın available_copies artır
        val book = supabase.postgrest["books"]
            .select { filter { eq("id", record.bookId) } }
            .decodeSingle<Book>()

        supabase.postgrest["books"]
            .update({
                set("available_copies", book.availableCopies + 1)
            }) {
                filter { eq("id", record.bookId) }
            }
    }
}