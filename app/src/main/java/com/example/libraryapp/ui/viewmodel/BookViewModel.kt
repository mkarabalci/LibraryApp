package com.example.libraryapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.libraryapp.data.model.Book
import com.example.libraryapp.data.repository.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class BookViewModel: ViewModel() {
    private val repository = BookRepository()

    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books : StateFlow<List<Book>> = _books

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error : StateFlow<String?> = _error

    private val _selectedBook = MutableStateFlow<Book?>(null)
    val selectedBook: StateFlow<Book?> = _selectedBook

    init {
        loadBooks()
    }

    fun loadBooks() {
        viewModelScope.launch {
            _isLoading.value = true
            repository
                .getAllBooks()
                .onSuccess { _books.value = it }
                .onFailure { _error.value = it.message }
            _isLoading.value = false
        }
    }

    fun getBookById(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            repository
                .getBookById(id)
                .onSuccess { _selectedBook.value = it }
                .onFailure { _error.value = it.message }
            _isLoading.value = false
        }
    }

    fun addBook(book: Book) {
        viewModelScope.launch {
            _isLoading.value = true
            repository
                .addBook(book)
                .onSuccess { loadBooks() } // ekleme sonrası listeyi yenile
                .onFailure { _error.value = it.message }
            _isLoading.value = false
        }
    }

    fun updateBook(id: String, book: Book) {
        viewModelScope.launch {
            _isLoading.value = true
            repository
                .updateBook(id, book)
                .onSuccess { loadBooks() }
                .onFailure { _error.value = it.message }
            _isLoading.value = false
        }
    }

    fun deleteBook(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            repository
                .deleteBook(id)
                .onSuccess { loadBooks() }
                .onFailure { _error.value = it.message }
            _isLoading.value = false
        }
    }

    fun searchBook(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            repository
                .searchBook(query)
                .onSuccess { _books.value = it }
                .onFailure { _error.value = it.message }
            _isLoading.value = false
        }
    }
}