package com.listentoprabhupada.android_ui.helpers

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.listentoprabhupada.android_ui.R
import com.arkivanov.decompose.value.MutableValue
import com.listentoprabhupada.android_ui.custom.SmallColumn
import com.listentoprabhupada.android_ui.custom.StandartRow
import com.listentoprabhupada.android_ui.theme.AppTheme
import com.listentoprabhupada.android_ui.theme.Colors.filtersCategory
import com.listentoprabhupada.android_ui.theme.Colors.filtersNeutral
import com.listentoprabhupada.android_ui.theme.Colors.filtersSelected
import com.listentoprabhupada.android_ui.theme.Colors.filtersText
import com.listentoprabhupada.android_ui.theme.Dimens.iconSizeL
import com.listentoprabhupada.android_ui.theme.Dimens.iconSizeM
import com.listentoprabhupada.android_ui.theme.Dimens.paddingM
import com.listentoprabhupada.android_ui.theme.Dimens.paddingS
import com.listentoprabhupada.android_ui.theme.Dimens.paddingXS
import com.listentoprabhupada.android_ui.theme.Dimens.paddingZero
import com.listentoprabhupada.android_ui.theme.Dimens.radiusS
import com.listentoprabhupada.android_ui.theme.Dimens.rowHeightL
import com.listentoprabhupada.common.filters_api.*

@Composable
fun FilterListItem(
    filter: Filter,
    component: FiltersComponent,
    isExpanded: MutableState<Boolean> = remember { mutableStateOf(filter.isExpanded) },
    modifier: Modifier = Modifier,
) =
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(paddingXS),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        FilterTitle(filter, isExpanded.value, modifier) {
            isExpanded.value = it
            component.onFilterExpanded(filter.name, it)
        }
        if (isExpanded.value) {
            filter.options.forEach { option ->
                OptionListItem(option, modifier) { isSelected ->
                    component.onQueryParam(
                        QueryParam(
                            filterName = filter.name,
                            selectedOption = option.value,
                            isSelected = isSelected
                        )
                    )
                }
            }
        }
    }

@Composable
fun FilterTitle(
    filter: Filter,
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
    onExpandedChanged: (Boolean) -> Unit
) =
    Box(
        modifier = modifier
            .height(rowHeightL)
            .background(
                color = filtersCategory(),
                shape = RoundedCornerShape(radiusS)
            )
            .clickable { onExpandedChanged(!isExpanded) }
            .padding(all = paddingS)
    ) {
        Image(
            painter = painterResource(if (isExpanded) R.drawable.ic_minus else R.drawable.ic_plus),
            contentScale = ContentScale.FillBounds,
            contentDescription = "expand filter",
            modifier =
            Modifier
                .align(Alignment.CenterStart)
                .size(iconSizeM)
        )

        Text(
            text = filter.title,
            color = filtersText(),
            style = typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center)
        )
    }

@Composable
fun OptionListItem(
    option: Option,
    modifier: Modifier = Modifier,
    onOptionSelected: ((Boolean) -> Unit)? = null
) =
    Box(
        modifier = modifier
            .height(rowHeightL)
            .background(
                color = option.background(),
                shape = RoundedCornerShape(4.dp)
            )
            .clickable { onOptionSelected?.invoke(!option.isSelected) }
    ) {
        Text(
            text = option.text,
            color = filtersText(),
            style = typography.titleLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(horizontal = paddingM)
                .align(Alignment.CenterStart)
        )
    }

@Composable
private fun Option.background() =
    if (isSelected) filtersSelected() else filtersNeutral()

@Composable
fun PreviewFilterListItem() {
    AppTheme {
        FilterListItem(
            getFilter(
                title = "Категория",
                options = listOf(
                    "Прогулки" to true,
                    "Интервью" to false,
                    "Беседы" to false,
                    "Инициации" to false,
                    "Лекции и обращения" to false,
                    "Интервью и пресс конференции asd asd asd sdf sdf" to false,
                )
            ),
            component = object: FiltersComponent {
                override val models = MutableValue(
                    FiltersState(
                        isLoading = false,
                        filters = emptyList(),
                        totalLecturesCount = 0,
                        pagesCount = 0
                    )
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

fun getFilter(title: String, options: List<Pair<String, Boolean>>) =
    Filter(
        title = title,
        options = options.map { getOption(it.first, it.second) }
    )

fun getOption(text: String, isSelected: Boolean = false) =
    Option(
        text = text,
        isSelected = isSelected
    )