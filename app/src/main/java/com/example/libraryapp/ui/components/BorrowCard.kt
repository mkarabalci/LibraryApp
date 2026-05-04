package com.example.libraryapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.libraryapp.data.model.BorrowRecord

@Composable
fun BorrowCard (
    record: BorrowRecord,
    onReturnClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = record.books?.title ?: "Kitap bulunamadı",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = record.books?.author ?: "",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = "İade tarihi: ${record.dueDate.take(10)}",
                fontSize = 12.sp
            )

            if(record.returnedAt == null)
            {
                Text(
                    text = "Aktif",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Button (
                    onClick = { onReturnClick() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("İade Et")
                }
            } else {
                Text(
                    text = "İade Edildi",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}