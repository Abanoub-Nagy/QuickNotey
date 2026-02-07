package com.example.noteyapp.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.noteyapp.feature.home.HomeViewModel
import com.example.noteyapp.data.db.NoteDatabase
import com.example.noteyapp.model.Note
import com.example.noteyapp.screen.ListNotesScreen
import kotlinx.coroutines.launch
import noteyapp.composeapp.generated.resources.Res
import noteyapp.composeapp.generated.resources.empty_logo
import noteyapp.composeapp.generated.resources.user
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    database: NoteDatabase, navController: NavController
) {
    val viewModel = viewModel {
        HomeViewModel(
            database
        )
    }
    val bottomSheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                showBottomSheet = true
            }, shape = CircleShape) {
                Text(
                    text = "+", fontSize = 24.sp
                )
            }
        }) { paddingValue ->
        val notes = viewModel.notes.collectAsStateWithLifecycle(emptyList())
        Column(
            modifier = Modifier.padding(paddingValue)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Notes",
                    fontWeight = Bold,
                    modifier = Modifier.fillMaxWidth().padding(16.dp).align(Alignment.CenterStart),
                    fontSize = 32.sp
                )
                Image(
                    painterResource(Res.drawable.user),
                    contentDescription = "User",
                    modifier = Modifier.padding(16.dp).clickable {
                        navController.navigate("signup")
                    }.align(Alignment.CenterEnd).size(52.dp)
                )
            }
            if (notes.value.isNotEmpty()) {
                ListNotesScreen(notes.value)
            } else {
                EmptyView()
            }
        }
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false }, sheetState = bottomSheetState
            ) {
                AddItemDialog(onCancel = {
                    coroutineScope.launch {
                        bottomSheetState.hide()
                    }
                    showBottomSheet = false
                }, {
                    viewModel.addNotes(it)
                    coroutineScope.launch {
                        bottomSheetState.hide()
                    }
                    showBottomSheet = false
                })
            }
        }
    }
}

@Composable
fun AddItemDialog(onCancel: () -> Unit, onSave: (Note) -> Unit) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        val colour = TextFieldDefaults.colors(
            focusedContainerColor = Transparent,
            unfocusedContainerColor = Transparent,
        )

        TextField(
            value = title,
            onValueChange = { title = it },
            colors = colour,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(text = "Title", fontSize = 22.sp)
            },
            textStyle = TextStyle(fontSize = 22.sp)
        )
        TextField(
            value = description,
            onValueChange = { description = it },
            colors = colour,
            placeholder = {
                Text(text = "Say something")
            },
            modifier = Modifier.fillMaxWidth(),
            minLines = 5
        )
        Row(modifier = Modifier.align(Alignment.End)) {
            Text(text = "Cancel", modifier = Modifier.padding(8.dp).clickable {
                onCancel()
            })
            Text(text = "Save", modifier = Modifier.padding(8.dp).clickable {
                onSave(
                    Note(
                        title = title, description = description
                    )
                )
            })
        }
    }
}

@Composable
fun EmptyView() {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxWidth().align(Alignment.Center)
        ) {
            Image(
                painterResource(Res.drawable.empty_logo),
                contentDescription = "Compose Multiplatform",
                modifier = Modifier.size(240.dp).align(Alignment.CenterHorizontally)
            )
            Text(
                text = "Create your first note!!",
                modifier = Modifier.align(
                    Alignment.CenterHorizontally
                ),
                fontSize = 24.sp,
            )
        }
    }
}