package com.example.libraryapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.libraryapp.data.model.BorrowRecord
import com.example.libraryapp.data.repository.BorrowRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BorrowViewModel(private val bookViewModel: BookViewModel) : ViewModel(){
    private val repository = BorrowRepository()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?> (null)
    val error: StateFlow<String?> = _error

    private val _borrowRecords = MutableStateFlow<List<BorrowRecord>> (emptyList())
    val borrowRecord: StateFlow<List<BorrowRecord>> = _borrowRecords

    private var currentStudentId: String? = null

    fun addBorrowRecord(studentId: String, bookId: String) {
        viewModelScope.launch {
            _isLoading.value = true


            val hasActive = repository.hasActiveBorrow(studentId, bookId)
            if (hasActive) {
                _error.value = "Bu kitabı zaten ödünç aldınız!"
                _isLoading.value = false
                return@launch
            }

            repository
                .addBorrowRecord(studentId, bookId)
                .onSuccess {
                    bookViewModel.loadBooks()  } // kitap listesini yenile
                .onFailure { _error.value = it.message }
            _isLoading.value = false

        }
    }

    fun resetError() {
        _error.value = null
    }

    fun loadBorrowRecords(studentId: String) {
        currentStudentId = studentId
        viewModelScope.launch {
            _isLoading.value = true
            repository
                .getBorrowRecords(studentId)
                .onSuccess { _borrowRecords.value = it }
                .onFailure { _error.value = it.message }
            _isLoading.value = false

        }
    }

    fun returnBook(borrowId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            repository
                .returnBook(borrowId)
                .onSuccess {
                    currentStudentId?.let { loadBorrowRecords(it) }
                }
                .onFailure { _error.value = it.message }
            _isLoading.value = false
        }
    }
}