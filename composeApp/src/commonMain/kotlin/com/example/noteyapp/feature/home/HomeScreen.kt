package com.example.noteyapp.feature.home

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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.noteyapp.model.Note
import com.example.noteyapp.screen.NoteList
import com.example.noteyapp.ui.theme.AccentGold
import com.example.noteyapp.ui.theme.BgCard
import com.example.noteyapp.ui.theme.BgDeep
import com.example.noteyapp.ui.theme.BgElevated
import com.example.noteyapp.ui.theme.BgSurface
import com.example.noteyapp.ui.theme.BorderSubtle
import com.example.noteyapp.ui.theme.TextMuted
import com.example.noteyapp.ui.theme.TextPrimary
import kotlinx.coroutines.launch
import noteyapp.composeapp.generated.resources.Res
import noteyapp.composeapp.generated.resources.empty_logo
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel, navController: NavController
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val authState by viewModel.authState.collectAsStateWithLifecycle()
    val notes by viewModel.notes.collectAsStateWithLifecycle(emptyList())

    Scaffold(
        containerColor = BgDeep, floatingActionButton = {
            FloatingActionButton(
                onClick = { showBottomSheet = true },
                shape = CircleShape,
                containerColor = AccentGold,
                contentColor = BgDeep,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 0.dp, pressedElevation = 6.dp
                )
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "New note",
                    modifier = Modifier.size(24.dp)
                )
            }
        }) { scaffoldPadding ->

        Box(
            modifier = Modifier.fillMaxSize().padding(scaffoldPadding)
        ) {
            Box(
                modifier = Modifier.fillMaxSize().background(
                    Brush.radialGradient(
                        colors = listOf(
                            AccentGold.copy(alpha = 0.05f), Color.Transparent
                        ), center = Offset(Float.POSITIVE_INFINITY, 0f), radius = 800f
                    )
                )
            )

            Column(modifier = Modifier.fillMaxSize()) {

                Row(
                    modifier = Modifier.fillMaxWidth().statusBarsPadding()
                        .padding(horizontal = 24.dp, vertical = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "notey.",
                            fontSize = 11.sp,
                            letterSpacing = 4.sp,
                            color = AccentGold,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(Modifier.size(4.dp))
                        Text(
                            text = "My Notes",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            letterSpacing = (-0.5).sp
                        )
                        if (!authState.email.isNullOrEmpty()) {
                            Text(
                                text = authState.email!!, fontSize = 12.sp, color = TextMuted
                            )
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (authState.isLoggedIn) {
                            TopBarIconButton(
                                onClick = { viewModel.performSync() }, contentDescription = "Sync"
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Sync,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp),
                                    tint = TextMuted
                                )
                            }
                        }
                        TopBarIconButton(
                            onClick = {
                                if (authState.isLoggedIn) navController.navigate("profile")
                                else navController.navigate("signup")
                            }, contentDescription = "Profile"
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Person,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = TextMuted
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier.fillMaxWidth().height(1.dp).background(BorderSubtle)
                )
                AnimatedVisibility(
                    visible = notes.isNotEmpty(), enter = fadeIn(tween(400))
                ) {
                    Text(
                        text = "${notes.size} note${if (notes.size != 1) "s" else ""}",
                        fontSize = 12.sp,
                        color = TextMuted,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
                    )
                }

                AnimatedVisibility(
                    visible = notes.isNotEmpty(), enter = fadeIn(), exit = fadeOut()
                ) {
                    NoteList(
                        list = notes, onDeleted = { note -> viewModel.deleteNote(note) })
                }
                AnimatedVisibility(
                    visible = notes.isEmpty(),
                    enter = fadeIn() + slideInVertically { it / 4 },
                    exit = fadeOut()
                ) {
                    EmptyView()
                }
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = bottomSheetState,
                containerColor = BgCard,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                dragHandle = {
                    Box(
                        modifier = Modifier.padding(vertical = 14.dp)
                            .size(width = 36.dp, height = 4.dp).clip(CircleShape)
                            .background(BorderSubtle)
                    )
                }) {
                AddNoteSheet(userId = authState.userId ?: "", onCancel = {
                    coroutineScope.launch { bottomSheetState.hide() }
                    showBottomSheet = false
                }, onSave = { note ->
                    viewModel.addNote(note)
                    coroutineScope.launch { bottomSheetState.hide() }
                    showBottomSheet = false
                })
            }
        }
    }
}

@Composable
private fun TopBarIconButton(
    onClick: () -> Unit, contentDescription: String, content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier.size(38.dp).clip(CircleShape).background(BgCard)
            .clickable(onClick = onClick), contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
fun AddNoteSheet(
    userId: String, onCancel: () -> Unit, onSave: (Note) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val transparentColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        focusedTextColor = TextPrimary,
        unfocusedTextColor = TextPrimary,
        cursorColor = AccentGold
    )

    Column(
        modifier = Modifier.fillMaxWidth().navigationBarsPadding().padding(horizontal = 4.dp)
            .padding(bottom = 20.dp)
    ) {
        Text(
            text = "NEW NOTE",
            fontSize = 10.sp,
            letterSpacing = 3.sp,
            color = AccentGold,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
        )

        TextField(
            value = title,
            onValueChange = { title = it },
            colors = transparentColors,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = "Title",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted
                )
            },
            textStyle = TextStyle(
                fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextPrimary
            ),
            singleLine = true
        )

        TextField(
            value = description,
            onValueChange = { description = it },
            colors = transparentColors,
            placeholder = {
                Text(
                    text = "Write something…", color = TextMuted, fontSize = 15.sp
                )
            },
            modifier = Modifier.fillMaxWidth(),
            minLines = 5,
            textStyle = TextStyle(fontSize = 15.sp, color = TextPrimary, lineHeight = 24.sp)
        )

        Box(
            modifier = Modifier.fillMaxWidth().height(1.dp).padding(horizontal = 16.dp)
                .background(BorderSubtle)
        )

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.End)
        ) {
            Box(
                modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(BgElevated)
                    .clickable { onCancel() }.padding(horizontal = 20.dp, vertical = 11.dp)
            ) {
                Text(
                    text = "Cancel",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextMuted
                )
            }

            Box(
                modifier = Modifier.clip(RoundedCornerShape(12.dp))
                    .background(if (title.isNotBlank()) AccentGold else BgElevated).clickable {
                        if (title.isNotBlank()) {
                            onSave(
                                Note(
                                    title = title,
                                    description = description,
                                    userId = userId,
                                    isDirty = true
                                )
                            )
                        }
                    }.padding(horizontal = 20.dp, vertical = 11.dp)
            ) {
                Text(
                    text = "Save",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (title.isNotBlank()) BgDeep else TextMuted
                )
            }
        }
    }
}


@Composable
fun EmptyView() {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier.size(96.dp).clip(CircleShape).background(
                    Brush.radialGradient(
                        colors = listOf(BgCard, BgSurface)
                    )
                ), contentAlignment = Alignment.Center
            ) {
                androidx.compose.foundation.Image(
                    painter = painterResource(Res.drawable.empty_logo),
                    contentDescription = null,
                    modifier = Modifier.size(48.dp)
                )
            }

            Text(
                text = "No notes yet",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
                letterSpacing = (-0.3).sp
            )
            Text(
                text = "Tap ✦ to capture your first thought",
                fontSize = 14.sp,
                color = TextMuted,
                textAlign = TextAlign.Center
            )

            Box(
                modifier = Modifier.size(6.dp).clip(CircleShape)
                    .background(AccentGold.copy(alpha = 0.5f))
            )
        }
    }
}