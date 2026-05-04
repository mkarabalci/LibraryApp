package com.example.libraryapp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.libraryapp.ui.components.BorrowCard
import com.example.libraryapp.ui.viewmodel.AuthViewModel
import com.example.libraryapp.ui.viewmodel.BorrowViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material.icons.automirrored.filled.ArrowBack


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BorrowScreen(
    authViewModel: AuthViewModel,
    borrowViewModel: BorrowViewModel,
    onNavigateBack: () -> Unit
) {
    val profile by authViewModel.profile.collectAsState()
    val borrowRecords by borrowViewModel.borrowRecord.collectAsState()
    val isLoading by borrowViewModel.isLoading.collectAsState()

    // sayfa açılınca kayıtları yükle
    LaunchedEffect(profile) {
        profile?.userId?.let { borrowViewModel.loadBorrowRecords(it) }
    }

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("KİRALAMALARIM") },
                navigationIcon = {
                    IconButton(onClick = { onNavigateBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Geri")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            val activeRecords = borrowRecords.filter { it.returnedAt == null }
            val pastRecords = borrowRecords.filter { it.returnedAt != null }

            when {
                isLoading -> CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
                borrowRecords.isEmpty() -> Text("Henüz kiralama yapılmamış.")
                else -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item { Text(text = "Aktif Kiralamalar", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold) }

                    if (activeRecords.isEmpty()) {
                        item { Text("Aktif kiralama yok.") }
                    } else {
                        items(activeRecords, key = { it.id!! }) { record ->
                            BorrowCard(
                                record = record,
                                onReturnClick = { borrowViewModel.returnBook(record.id!!) }
                            )
                        }
                    }

                    item { Text(text = "Geçmiş Kiralamalar", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold) }

                    if (pastRecords.isEmpty()) {
                        item { Text("Geçmiş kiralama yok.") }
                    } else {
                        items(pastRecords, key = { it.id!! }) { record ->
                            BorrowCard(
                                record = record,
                                onReturnClick = { borrowViewModel.returnBook(record.id!!) }
                            )
                        }
                   }
                }
            }
        }
    }
}