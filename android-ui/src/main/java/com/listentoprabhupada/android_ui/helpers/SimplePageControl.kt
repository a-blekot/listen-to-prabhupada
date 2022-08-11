package com.listentoprabhupada.android_ui.helpers

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.listentoprabhupada.android_ui.custom.StandartRow
import com.listentoprabhupada.android_ui.theme.Colors
import com.listentoprabhupada.android_ui.theme.Dimens.paddingXS
import com.listentoprabhupada.android_ui.theme.Dimens.radiusS
import com.listentoprabhupada.android_ui.theme.Dimens.rowHeightM
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
) =
    Button(
        modifier = Modifier.weight(2f).height(rowHeightM),
        enabled = pagination.canAdd(buttonType),
        onClick = { component.onPage(pagination.nextPage(buttonType)) },
        shape = RoundedCornerShape(radiusS),
        colors = ButtonDefaults.buttonColors(
            containerColor = Colors.btnPages(),
        ),
        contentPadding = PaddingValues(horizontal = paddingXS)
    ) {
        Text(
            text = stringResource(buttonType.textRes),
            maxLines = 1,
            color = Colors.tertiary(),
            style = MaterialTheme.typography.titleSmall,
        )
    }
