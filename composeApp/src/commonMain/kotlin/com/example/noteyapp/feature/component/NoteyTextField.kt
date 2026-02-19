package com.example.noteyapp.feature.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.noteyapp.ui.theme.AccentGold
import com.example.noteyapp.ui.theme.BgSurface
import com.example.noteyapp.ui.theme.BorderSubtle
import com.example.noteyapp.ui.theme.ErrorRed
import com.example.noteyapp.ui.theme.TextMuted
import com.example.noteyapp.ui.theme.TextPrimary

@Composable
fun NoteyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    isError: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label, color = TextMuted, fontSize = 13.sp) },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        singleLine = true,
        isError = isError,
        supportingText = supportingText,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary,
            cursorColor = AccentGold,
            focusedBorderColor = AccentGold.copy(alpha = 0.8f),
            unfocusedBorderColor = BorderSubtle,
            errorBorderColor = ErrorRed.copy(alpha = 0.7f),
            focusedContainerColor = BgSurface,
            unfocusedContainerColor = BgSurface,
            errorContainerColor = ErrorRed.copy(alpha = 0.05f),
            focusedLabelColor = AccentGold,
            unfocusedLabelColor = TextMuted
        )
    )
}