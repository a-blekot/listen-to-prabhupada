package com.anadi.prabhupadalectures.android.ui.compose

import android.text.Layout
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.anadi.prabhupadalectures.android.R
import com.anadi.prabhupadalectures.android.ui.screens.results.ResultsEvent
import com.anadi.prabhupadalectures.datamodel.Pagination

@Composable
fun PageControl(
    pagination: Pagination,
    total: Int,
    modifier: Modifier = Modifier,
    onEvent: (ResultsEvent) -> Unit = {}
) =
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            modifier = modifier.weight(0.33f),
            enabled = pagination.prev != null,
            onClick = { onEvent(ResultsEvent.Page(pagination.prev ?: pagination.curr - 1)) },
            colors = ButtonDefaults.textButtonColors(
                backgroundColor = MaterialTheme.colors.primaryVariant,
            ),
        ) {
            Text(
                text = stringResource(R.string.previous_page),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colors.onSecondary,
                style = MaterialTheme.typography.body2
            )
        }
//        PageInput(
//            pagination.curr,
//            total,
//            modifier = modifier
//                .weight(0.2f)
//                .padding(start = 4.dp, end = 4.dp),
//        )
        Text(
            text = "${pagination.curr} из ${total / 6 + if (total % 6 == 0) 0 else 1}",
            textAlign = TextAlign.Center,
            modifier = modifier
                .weight(0.33f)
                .padding(start = 4.dp, end = 4.dp)
        )
        Button(
            modifier = modifier.weight(0.33f),
            enabled = pagination.next != null,
            onClick = { onEvent(ResultsEvent.Page(pagination.next ?: pagination.curr + 1)) },
            colors = ButtonDefaults.textButtonColors(
                backgroundColor = MaterialTheme.colors.primaryVariant,
            )
        ) {
            Text(
                text = stringResource(R.string.next_page),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colors.onSecondary,
                style = MaterialTheme.typography.body2
            )
        }
    }

@Composable
fun PageInput(
    page: Int,
    total: Int,
    modifier: Modifier = Modifier,
    onEvent: (ResultsEvent) -> Unit = {}
) {
    var text by remember { mutableStateOf(TextFieldValue("$page")) }
    TextField(
        value = text,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        onValueChange = {
            val page = (it.text.toIntOrNull() ?: 1).coerceIn(1, total)
            onEvent(ResultsEvent.Page(page))
            text = it.copy(text = "$page")
        },
        singleLine = true,
        modifier = modifier
    )
}

@Preview
@Composable
fun PageControlPreview() =
    PageControl(Pagination(curr = 1, next = 2), 830)
