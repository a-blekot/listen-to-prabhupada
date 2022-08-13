package com.listentoprabhupada.android_ui.helpers

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import com.listentoprabhupada.android_ui.R
import com.listentoprabhupada.android_ui.theme.Colors.chipsBg
import com.listentoprabhupada.android_ui.theme.Colors.chipsContent
import com.listentoprabhupada.android_ui.theme.Dimens.borderXS
import com.listentoprabhupada.android_ui.theme.Dimens.iconSizeS
import com.listentoprabhupada.android_ui.theme.Dimens.paddingXS
import com.listentoprabhupada.common.filters_api.Filter
import com.listentoprabhupada.common.filters_api.FiltersComponent
import com.listentoprabhupada.common.filters_api.QueryParam

@Composable
fun SelectedFilters(
    filters: List<Filter>,
    component: FiltersComponent,
    modifier: Modifier = Modifier,
) =
    FlowLayout(
        modifier = modifier,
    ) {
        var hasSelectedFilters = false
        filters
            .filter {
                it.options.any { option -> option.isSelected }
            }
            .forEach { filter ->
                filter.options.firstOrNull { it.isSelected }?.let { option ->
                    hasSelectedFilters = true
                    FilterChip(option.text) {
                        component.onQueryParam(
                            QueryParam(
                                filterName = filter.name,
                                selectedOption = option.value,
                                isSelected = false
                            )
                        )
                    }
                }
            }

        if (hasSelectedFilters) {
            FilterChip(stringResource(R.string.clear_all)) {
                component.onClearAll()
            }
        }
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterChip(
    text: String,
    onClick: () -> Unit = {}
) =
    InputChip(
        selected = true,
        onClick = onClick,
        trailingIcon = { ClearImage() },
        border = InputChipDefaults.inputChipBorder(
            borderColor = chipsContent(),
            selectedBorderColor = chipsContent(),
            borderWidth = borderXS,

        ),
        colors = InputChipDefaults.inputChipColors(
            containerColor = chipsBg(),
            selectedContainerColor = chipsBg(),
            labelColor = chipsContent(),
            leadingIconColor = chipsContent(),
            trailingIconColor = chipsContent(),
            selectedLabelColor = chipsContent(),
            selectedLeadingIconColor = chipsContent(),
            selectedTrailingIconColor = chipsContent(),
        ),
        modifier = Modifier.padding(end = paddingXS),
        label = { Text(text, style = MaterialTheme.typography.titleSmall) }
    )

@Composable
fun ClearImage() =
    Icon(
        imageVector = Icons.Default.Clear,
        contentDescription = "delete filter chip",
        modifier = Modifier.size(iconSizeS)
    )

class RowInfo(val width: Int, val height: Int, val nextChildIndex: Int)

@Composable
fun FlowLayout(
    modifier: Modifier = Modifier,
    rowHorizontalGravity: Alignment.Horizontal = Alignment.Start,
    childVerticalGravity: Alignment.Vertical = Alignment.Top,
    content: @Composable () -> Unit
) {
    val layoutDirection = LocalLayoutDirection.current

    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        var contentWidth = 0
        var contentHeight = 0
        var rowWidth = 0
        var rowHeight = 0
        val rows = mutableListOf<RowInfo>()
        val maxWidth = constraints.maxWidth
        val childConstraints = constraints.copy(minWidth = 0, minHeight = 0)
        val placeables = measurables.mapIndexed { index, measurable ->
            measurable.measure(childConstraints).also { placeable ->
                val newRowWidth = rowWidth + placeable.width
                if (newRowWidth > maxWidth) {
                    rows.add(RowInfo(width = rowWidth, height = rowHeight, nextChildIndex = index))
                    contentWidth = maxOf(contentWidth, rowWidth)
                    contentHeight += rowHeight
                    rowWidth = placeable.width
                    rowHeight = placeable.height
                } else {
                    rowWidth = newRowWidth
                    rowHeight = maxOf(rowHeight, placeable.height)
                }
            }
        }
        rows.add(RowInfo(width = rowWidth, height = rowHeight, nextChildIndex = measurables.size))
        contentWidth = maxOf(contentWidth, rowWidth)
        contentHeight += rowHeight

        layout(
            width = maxOf(contentWidth, constraints.minWidth),
            height = maxOf(contentHeight - 16, constraints.minHeight)
        ) {
            var childIndex = 0
            var y = 0
            rows.forEach { rowInfo ->
                var x = rowHorizontalGravity.align(
                    constraints.maxWidth - rowInfo.width,
                    0,
                    layoutDirection
                )
                val rowHeight = rowInfo.height - 16
                val nextChildIndex = rowInfo.nextChildIndex
                while (childIndex < nextChildIndex) {
                    val placeable = placeables[childIndex]
                    placeable.placeRelative(
                        x = x,
                        y = y + childVerticalGravity.align(rowHeight - placeable.height, 0)
                    )
                    x += placeable.width
                    childIndex++
                }
                y += rowHeight
            }
        }
    }
}