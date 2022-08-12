package com.listentoprabhupada.android_ui.helpers

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.listentoprabhupada.android_ui.custom.StandartRow
import com.listentoprabhupada.android_ui.theme.Colors
import com.listentoprabhupada.android_ui.theme.Colors.btnPages
import com.listentoprabhupada.android_ui.theme.Colors.primary
import com.listentoprabhupada.android_ui.theme.Dimens.borderXS
import com.listentoprabhupada.android_ui.theme.Dimens.buttonHeight
import com.listentoprabhupada.android_ui.theme.Dimens.paddingXS
import com.listentoprabhupada.android_ui.theme.Dimens.radiusXL
import com.listentoprabhupada.common.results_api.Pagination
import com.listentoprabhupada.common.results_api.ResultsComponent

@Composable
fun SimplePageControl(
    pagination: Pagination,
    component: ResultsComponent,
    modifier: Modifier = Modifier,
) =
    StandartRow(modifier) {

        PageButton(
            ButtonType.PREV,
            pagination,
            component,
        )

        Text(
            text = "${pagination.curr} из ${pagination.total}",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleSmall,
            color = Colors.tertiary(),
            modifier = modifier
                .weight(1f)
                .padding(horizontal = paddingXS)
        )

        PageButton(
            ButtonType.NEXT,
            pagination,
            component,
        )
    }

@Composable
private fun RowScope.PageButton(
    buttonType: ButtonType,
    pagination: Pagination,
    component: ResultsComponent
) {
    val enabled = pagination.canAdd(buttonType)
    Box(
        modifier = Modifier
            .weight(1.3f)
            .height(buttonHeight)
            .background(
                color = if (enabled) btnPages() else Transparent,
                shape = RoundedCornerShape(radiusXL)
            )
            .border(
                width = borderXS,
                color = primary(),
                shape = RoundedCornerShape(radiusXL)
            )
            .clickable { component.onPage(pagination.nextPage(buttonType)) },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(buttonType.textRes),
            maxLines = 1,
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Composable
fun SimplePageControlPreview() =
    SimplePageControl(
        Pagination(prev = null, curr = 1, next = 2, total = 180),
        ResultsComponentStub
    )

