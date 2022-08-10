package com.listentoprabhupada.android_ui.theme

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults.outlinedTextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun textFieldColors(
    textColor: Color = MaterialTheme.colorScheme.primary
) =
    outlinedTextFieldColors(
          textColor = textColor,
    )