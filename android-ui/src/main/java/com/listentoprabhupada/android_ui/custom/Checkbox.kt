package com.listentoprabhupada.android_ui.custom

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckBox
import androidx.compose.material.icons.rounded.CheckBoxOutlineBlank
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.listentoprabhupada.android_ui.theme.Dimens.iconSizeL

@Composable
fun StandartCheckBox(
    isSelected: Boolean,
    color: Color = colorScheme.primary,
    onChanged: (Boolean) -> Unit
) =
    IconButton(
        onClick = { onChanged(!isSelected) },
        modifier = Modifier.size(iconSizeL),
    ) {
        Icon(
            if (isSelected) Icons.Rounded.CheckBox else Icons.Rounded.CheckBoxOutlineBlank,
            "isSelected",
            tint = color,
            modifier = Modifier.fillMaxSize()
        )
    }