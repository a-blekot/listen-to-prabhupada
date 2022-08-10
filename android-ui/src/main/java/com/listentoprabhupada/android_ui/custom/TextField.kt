package com.listentoprabhupada.android_ui.custom

import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckBox
import androidx.compose.material.icons.rounded.CheckBoxOutlineBlank
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import com.listentoprabhupada.android_ui.theme.textFieldColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextField(
    initialText: String,
    label: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    maxLines: Int = 1,
    leadingIcon: @Composable (() -> Unit)? = null,
    onSaveChanges: (String) -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next)
) {
    val text = remember { mutableStateOf(TextFieldValue(text = initialText)) }
    val hasChanges = text.value.text != initialText

    OutlinedTextField(
        value = text.value,
        onValueChange = { text.value = it },
        label = { Text(text = label, style = MaterialTheme.typography.titleSmall) },
        placeholder = { Text(text = placeholder, style = MaterialTheme.typography.titleLarge) },
        textStyle = MaterialTheme.typography.titleLarge,
        maxLines = maxLines,
        colors = textFieldColors(),
        keyboardOptions = keyboardOptions,
        leadingIcon = leadingIcon,
        trailingIcon = {
            Icon(
                imageVector = if (hasChanges)
                    Icons.Rounded.CheckBoxOutlineBlank
                else
                    Icons.Rounded.CheckBox,
                contentDescription = "check icon",
                modifier = modifier.clickable {
                    onSaveChanges(text.value.text)
                }
            )
        }
    )
}