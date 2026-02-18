package com.example.noteyapp.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.noteyapp.model.Note
import com.example.noteyapp.utils.DateUtils
import noteyapp.composeapp.generated.resources.Res
import noteyapp.composeapp.generated.resources.delete
import org.jetbrains.compose.resources.painterResource
import kotlin.time.Instant

@Composable
fun ListNotesScreen(list: List<Note>, onDeleted: (Note) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(list) {
            NoteItem(it, onDeleted)
        }
    }
}

fun getRandomColor(): Color {
    return Color((155..255).random(), (155..255).random(), (155..255).random())
}

@Composable
fun NoteItem(note: Note, onDeleted: (Note) -> Unit) {
    Column(
        modifier = Modifier.padding(
            8.dp,
        ).fillMaxWidth().clip(shape = RoundedCornerShape(8.dp)).background(getRandomColor())
            .padding(16.dp)
    ) {
        Row(verticalAlignment = CenterVertically) {
            Text(
                text = note.title, fontSize = 24.sp, color = Black, modifier = Modifier.weight(1f)
            )
            Image(
                painterResource(Res.drawable.delete),
                contentDescription = "Delete",
                modifier = Modifier.padding(8.dp).size(48.dp).clickable {
                    onDeleted.invoke(note)
                },
            )
        }

        Spacer(modifier = Modifier.size(8.dp))
        Text(text = note.description, fontSize = 16.sp, color = Black)
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = DateUtils.formatDate(Instant.parse(note.updatedAt), "dd MMM, yyyy - HH:mm a"),
            fontSize = 16.sp,
            color = Black,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End
        )
    }
}