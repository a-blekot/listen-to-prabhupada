package com.listentoprabhupada.android_ui.helpers

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.listentoprabhupada.android_ui.R
import com.listentoprabhupada.android_ui.theme.Dimens.paddingXS
import com.listentoprabhupada.common.filters_api.Filter
import com.listentoprabhupada.common.filters_api.FiltersComponent
import com.listentoprabhupada.common.filters_api.Option
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
                    FilterChip(
                        filterName = filter.name,
                        option = option,
                    ) {
                        component.onQueryParam(it)
                    }
                }
            }

        if (hasSelectedFilters) {
            ClearAllFiltersChip { component.onClearAll() }
        }
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterChip(
    filterName: String,
    option: Option,
    onClick: (QueryParam) -> Unit = {}
) =
    InputChip(
        selected = true,
        onClick = {
            onClick(
                QueryParam(
                    filterName = filterName,
                    selectedOption = option.value,
                    isSelected = false
                )
            )
        },
        leadingIcon = { ClearImage() },
        modifier = Modifier.padding(end = paddingXS),
        label = { Text(option.text) }
    )

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClearAllFiltersChip(onClick: () -> Unit = {}) =
    InputChip(
        selected = true,
        onClick = onClick,
        leadingIcon = { ClearImage() },
        label = { Text(text = stringResource(R.string.clear_all)) }
    )

@Composable
fun ClearImage() =
    Image(
        imageVector = Icons.Default.Clear, // Close, Clear,
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
        contentDescription = "delete filter chip",
        modifier = Modifier.wrapContentWidth()
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