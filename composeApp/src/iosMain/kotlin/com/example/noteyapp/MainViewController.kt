package com.example.noteyapp

import androidx.compose.ui.window.ComposeUIViewController
import com.example.noteyapp.data.db.getNoteDatabase

fun MainViewController() = ComposeUIViewController { App(
    getNoteDatabase(getDatabaseBuilder())
) }