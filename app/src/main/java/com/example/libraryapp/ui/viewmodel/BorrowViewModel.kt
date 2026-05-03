package com.example.libraryapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
}