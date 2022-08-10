package com.listentoprabhupada.android_ui.custom

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.listentoprabhupada.android_ui.theme.Dimens.horizontalScreenPadding
import com.listentoprabhupada.android_ui.theme.Dimens.paddingXS
import com.listentoprabhupada.android_ui.theme.Dimens.paddingZero

@Composable
fun StandartRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceEvenly,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    padding: Dp = paddingXS,
    content: @Composable RowScope.() -> Unit
) =
    Row(
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment,
        modifier = modifier
            .fillMaxWidth()
            .padding(padding),
        content = content
    )

@Composable
fun StandartColumn(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(paddingXS),
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    padding: Dp = paddingZero,
    content: @Composable ColumnScope.() -> Unit
) =
    Column(
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        modifier = modifier
            .fillMaxSize()
            .padding(padding),
        content = content
    )

@Composable
fun SmallColumn(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(paddingXS),
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    padding: Dp = paddingZero,
    content: @Composable ColumnScope.() -> Unit
) =
    Column(
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        modifier = modifier
            .fillMaxWidth()
            .padding(padding),
        content = content
    )

@Composable
fun StandartLazyColumn(
    modifier: Modifier = Modifier,
    itemPadding: Dp = paddingXS,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    content: LazyListScope.() -> Unit
) =
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalScreenPadding),
        verticalArrangement = Arrangement.spacedBy(itemPadding),
        horizontalAlignment = horizontalAlignment,
        content = content
    )