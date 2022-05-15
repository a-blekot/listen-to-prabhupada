package com.prabhupadalectures.android.ui.screens.helpers

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import com.arkivanov.decompose.value.MutableValue
import com.prabhupadalectures.android.R
import com.prabhupadalectures.common.filters.FiltersComponent
import com.prabhupadalectures.common.filters.FiltersState
import com.prabhupadalectures.common.filters.data.Filter
import com.prabhupadalectures.common.filters.data.Option
import com.prabhupadalectures.common.filters.data.QueryParam

@Composable
fun FilterListItem(
    component: FiltersComponent,
    filter: Filter,
    isExpanded: MutableState<Boolean> = remember { mutableStateOf(filter.isExpanded) },
    modifier: Modifier = Modifier,
) =
    Column(
        modifier = modifier.padding(top = 8.dp),
    ) {
        FilterTitle(filter, isExpanded.value) {
            isExpanded.value = it
            component.onFilterExpanded(filter.name, it)
        }
        if (isExpanded.value) {
            filter.options.forEach { option ->
                OptionListItem(option) { isSelected ->
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
fun FilterTitle(filter: Filter, isExpanded: Boolean, onExpandedChanged: (Boolean) -> Unit) =
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(bottom = if (isExpanded) 4.dp else 0.dp)
            .background(
                color = MaterialTheme.colors.primary,
                shape = RoundedCornerShape(4.dp)
            )
            .clickable { onExpandedChanged(!isExpanded) }
            .padding(all = 8.dp)
    ) {
        Image(
            painter = painterResource(if (isExpanded) R.drawable.ic_minus else R.drawable.ic_plus),
            contentScale = ContentScale.FillBounds,
            contentDescription = "expand filter",
            modifier =
            Modifier
                .align(Alignment.CenterStart)
                .size(24.dp)
        )

        Text(
            text = filter.title,
            color = MaterialTheme.colors.onPrimary,
            fontSize = 18.sp,
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center)
        )
    }

@Composable
fun OptionListItem(
    option: Option,
    onOptionSelected: ((Boolean) -> Unit)? = null
) =
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(bottom = 4.dp)
            .background(
                color = MaterialTheme.colors.secondary,
                shape = RoundedCornerShape(4.dp)
            )
            .clickable { onOptionSelected?.invoke(!option.isSelected) }
            .padding(all = 4.dp)

    ) {
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = option.text,
            color = MaterialTheme.colors.onSecondary,
            fontSize = 18.sp,
            style = MaterialTheme.typography.h6,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1.0f)
        )
        Checkbox(
            checked = option.isSelected,
            onCheckedChange = null, // { onOptionSelected?.invoke(it) },
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colors.onSecondary,
                checkmarkColor = MaterialTheme.colors.secondary,
            )
        )
        Spacer(modifier = Modifier.width(16.dp))
    }

@Composable
fun PreviewFilterListItem() {
    AppTheme {
        FilterListItem(
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