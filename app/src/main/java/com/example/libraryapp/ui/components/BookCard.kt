package com.example.libraryapp.ui.components

import android.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.libraryapp.data.model.Book

@Composable
fun BookCard(book: Book) {
    Card(
        modifier = Modifier.fillMaxWidth(),

    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = book.title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = book.author,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = book.category,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "${book.availableCopies}/${book.totalCopies} mevcut",
                fontSize = 11.sp
            )
            if (book.availableCopies >0) {
                Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                    Text("Ödünç Al")
                }
            } else {
                Text(
                    text = "Stokta yok",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp
                )
            }

        }
    }
}