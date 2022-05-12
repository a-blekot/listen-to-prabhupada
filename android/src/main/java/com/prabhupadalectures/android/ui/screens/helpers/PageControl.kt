package com.prabhupadalectures.android.ui.screens.helpers

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.prabhupadalectures.android.R
import com.prabhupadalectures.common.lectures_api.LecturesComponent
import com.prabhupadalectures.common.lectures_api.Pagination

private const val WEIGHT_BUTTON = 1f

enum class ButtonType(val pagesDiff: Int, @StringRes val textRes: Int) {
    FIRST(0, R.string.prev_page_first),
    PREV_20(-20, R.string.prev_page_20),
    PREV_5(-5, R.string.prev_page_5),
    PREV_1(-1, R.string.prev_page_1),
    NEXT_1(1, R.string.next_page_1),
    NEXT_5(5, R.string.next_page_5),
    NEXT_20(20, R.string.next_page_20),
    LAST(0, R.string.next_page_last)
}

@Composable
fun PageControl(
    component: LecturesComponent,
    pagination: Pagination,
    modifier: Modifier = Modifier,
) =
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {

        PageImageButton(
            ButtonType.FIRST,
            pagination,
            component,
        )

        PageButton(
            ButtonType.PREV_20,
            pagination,
            component,
        )

        PageButton(
            ButtonType.PREV_5,
            pagination,
            component,
        )

        PageButton(
            ButtonType.PREV_1,
            pagination,
            component,
        )

        Text(
            text = "${pagination.curr} из ${pagination.total}",
            textAlign = TextAlign.Center,
            modifier = modifier
                .wrapContentWidth()
                .padding(start = 4.dp, end = 4.dp)
        )

        PageButton(
            ButtonType.NEXT_1,
            pagination,
            component,
        )

        PageButton(
            ButtonType.NEXT_5,
            pagination,
            component,
        )

        PageButton(
            ButtonType.NEXT_20,
            pagination,
            component,
        )

        PageImageButton(
            ButtonType.LAST,
            pagination,
            component,
        )
    }

@Composable
fun RowScope.PageButton(
    buttonType: ButtonType,
    pagination: Pagination,
    component: LecturesComponent
) =
    Button(
        modifier = Modifier.weight(WEIGHT_BUTTON).padding(horizontal = 1.dp),
        enabled = pagination.canAdd(buttonType),
        onClick = { component.onPage(pagination.nextPage(buttonType)) },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.primaryVariant,
        ),
        contentPadding = PaddingValues(horizontal = 2.dp)
    ) {
        Text(
            text = stringResource(buttonType.textRes),
            maxLines = 1,
            color = MaterialTheme.colors.onSecondary,
            style = MaterialTheme.typography.body2,
        )
    }

@Composable
fun RowScope.PageImageButton(
    buttonType: ButtonType,
    pagination: Pagination,
    component: LecturesComponent,
) =
    Button(
        modifier = Modifier.weight(WEIGHT_BUTTON).padding(horizontal = 1.dp),
        enabled = pagination.canAdd(buttonType),
        onClick = { component.onPage(pagination.nextPage(buttonType)) },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.primaryVariant,
        ),
        contentPadding = PaddingValues(horizontal = 2.dp)
    ) {
        Image(
            painter = painterResource(if (buttonType == ButtonType.FIRST) R.drawable.ic_first_page else R.drawable.ic_last_page),
            contentDescription = "page button",
            contentScale = ContentScale.Inside,
        )
    }

fun Pagination.canAdd(buttonType: ButtonType) =
    when (buttonType) {
        ButtonType.FIRST -> curr > com.prabhupadalectures.common.settings.FIRST_PAGE
        ButtonType.LAST -> curr < total
        else -> canAdd(buttonType.pagesDiff)
    }

private fun Pagination.nextPage(buttonType: ButtonType) =
    when (buttonType) {
        ButtonType.FIRST -> com.prabhupadalectures.common.settings.FIRST_PAGE
        ButtonType.LAST -> total
        else -> add(buttonType.pagesDiff)
    }

//@Preview
//@Composable
//fun PageControlPreview() =
//    PageControl(Pagination(prev = 121, curr = 122, next = 123, total = 180))
