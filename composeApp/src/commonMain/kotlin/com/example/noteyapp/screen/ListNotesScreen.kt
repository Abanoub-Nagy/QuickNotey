package com.example.noteyapp.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.noteyapp.model.Note
import com.example.noteyapp.ui.theme.BgCard
import com.example.noteyapp.ui.theme.BgElevated
import com.example.noteyapp.ui.theme.BorderSubtle
import com.example.noteyapp.ui.theme.CardTints
import com.example.noteyapp.ui.theme.TextMuted
import com.example.noteyapp.ui.theme.TextPrimary
import com.example.noteyapp.utils.DateUtils
import kotlin.time.Instant


// ── List ──────────────────────────────────────────────────────────────────────

@Composable
fun NoteList(list: List<Note>, onDeleted: (Note) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        itemsIndexed(list, key = { _, note -> note.id ?: note.title }) { index, note ->
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(tween(300, delayMillis = (index * 40).coerceAtMost(300))) +
                        slideInVertically(
                            tween(
                                300,
                                delayMillis = (index * 40).coerceAtMost(300)
                            )
                        ) { it / 6 }
            ) {
                NoteItem(
                    note = note,
                    tint = CardTints[index % CardTints.size],
                    onDeleted = onDeleted
                )
            }
        }
    }
}

// ── Item ──────────────────────────────────────────────────────────────────────

@Composable
fun NoteItem(note: Note, tint: Color = BgCard, onDeleted: (Note) -> Unit) {
    var confirmDelete by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(tint, BgCard)
                )
            )
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // ── Header row ────────────────────────────────────────────────────────
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = note.title,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            // Delete / Confirm
            AnimatedVisibility(visible = !confirmDelete, exit = fadeOut(tween(150))) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(BgElevated)
                        .clickable { confirmDelete = true },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.DeleteOutline,
                        contentDescription = "Delete",
                        tint = TextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            AnimatedVisibility(visible = confirmDelete, enter = fadeIn(tween(150))) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    // Confirm
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF3A1E1E))
                            .clickable {
                                confirmDelete = false
                                onDeleted(note)
                            }
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            "Delete",
                            fontSize = 12.sp,
                            color = Color(0xFFE05C5C),
                            fontWeight = FontWeight.Medium
                        )
                    }
                    // Cancel
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(BgElevated)
                            .clickable { confirmDelete = false }
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            "Cancel",
                            fontSize = 12.sp,
                            color = TextMuted,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // ── Description ───────────────────────────────────────────────────────
        if (note.description.isNotBlank()) {
            Spacer(Modifier.size(8.dp))
            Text(
                text = note.description,
                fontSize = 14.sp,
                color = TextMuted,
                lineHeight = 20.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }

        // ── Footer ────────────────────────────────────────────────────────────
        Spacer(Modifier.size(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(BorderSubtle)
        )

        Spacer(Modifier.size(10.dp))

        Text(
            text = DateUtils.formatDate(Instant.parse(note.updatedAt), "dd MMM yyyy · HH:mm"),
            fontSize = 11.sp,
            color = TextMuted.copy(alpha = 0.7f),
            letterSpacing = 0.3.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = androidx.compose.ui.text.style.TextAlign.End
        )
    }
}