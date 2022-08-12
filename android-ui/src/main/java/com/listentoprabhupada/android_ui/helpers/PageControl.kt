package com.listentoprabhupada.android_ui.helpers

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.listentoprabhupada.common.results_api.ResultsComponent
import com.listentoprabhupada.android_ui.R
import com.listentoprabhupada.android_ui.custom.StandartRow
import com.listentoprabhupada.android_ui.theme.Colors.btnPages
import com.listentoprabhupada.android_ui.theme.Colors.tertiary
import com.listentoprabhupada.android_ui.theme.Dimens.paddingXS
import com.listentoprabhupada.common.results_api.Pagination
import com.listentoprabhupada.common.results_api.ResultsState
import com.listentoprabhupada.common.settings.FIRST_PAGE

private const val WEIGHT_BUTTON = 1f

enum class ButtonType(val pagesDiff: Int, @StringRes val textRes: Int) {
    FIRST(0, R.string.prev_page_first),
    PREV_20(-20, R.string.prev_page_20),
    PREV_5(-5, R.string.prev_page_5),
    PREV_1(-1, R.string.prev_page_1),
    PREV(-1, R.string.previous_page),
    NEXT(1, R.string.next_page),
    NEXT_1(1, R.string.next_page_1),
    NEXT_5(5, R.string.next_page_5),
    NEXT_20(20, R.string.next_page_20),
    LAST(0, R.string.next_page_last)
}

@Composable
fun PageControl(
    pagination: Pagination,
    component: ResultsComponent,
    modifier: Modifier = Modifier,
) =
    StandartRow(modifier = modifier,) {

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
            style = typography.titleSmall,
            color = tertiary(),
            modifier = modifier
                .padding(horizontal = paddingXS)
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
private fun PageButton(
    buttonType: ButtonType,
    pagination: Pagination,
    component: ResultsComponent
) =
    Button(
        modifier = Modifier.size(30.dp),
        enabled = pagination.canAdd(buttonType),
        onClick = { component.onPage(pagination.nextPage(buttonType)) },
        colors = ButtonDefaults.buttonColors(
            containerColor = btnPages(),
        ),
        contentPadding = PaddingValues(horizontal = paddingXS)
    ) {
        Text(
            text = stringResource(buttonType.textRes),
            maxLines = 1,
            textAlign = TextAlign.Center,
            color = tertiary(),
            style = typography.bodySmall,
        )
    }

@Composable
private fun PageImageButton(
    buttonType: ButtonType,
    pagination: Pagination,
    component: ResultsComponent,
) {
    val enabled = pagination.canAdd(buttonType)
    Icon(
        imageVector = if (buttonType == ButtonType.FIRST) Icons.Rounded.ChevronLeft else Icons.Rounded.ChevronRight,
        contentDescription = "page button",
        modifier = Modifier
            .size(30.dp)
            .alpha(if (enabled) 1f else 0.5f)
            .background(
                color = btnPages(),
                shape = CircleShape
            )
            .clickable {
                if (enabled) component.onPage(pagination.nextPage(buttonType))
            },
        tint = tertiary()
    )
}

fun Pagination.canAdd(buttonType: ButtonType) =
    when (buttonType) {
        ButtonType.FIRST -> curr > FIRST_PAGE
        ButtonType.LAST -> curr < total
        else -> canAdd(buttonType.pagesDiff)
    }

fun Pagination.nextPage(buttonType: ButtonType) =
    when (buttonType) {
        ButtonType.FIRST -> FIRST_PAGE
        ButtonType.LAST -> total
        else -> add(buttonType.pagesDiff)
    }

@Preview
@Composable
fun PageControlPreview() =
    PageControl(Pagination(prev = 121, curr = 122, next = 123, total = 180), ResultsComponentStub)

object ResultsComponentStub : ResultsComponent {
    override val flow: Value<ResultsState> =
        MutableValue(ResultsState())
}
