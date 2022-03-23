package com.anadi.prabhupadalectures.android.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.anadi.prabhupadalectures.android.R
import com.anadi.prabhupadalectures.android.ui.screens.CommonUiEvent
import com.anadi.prabhupadalectures.data.filters.Filter
import com.anadi.prabhupadalectures.data.filters.Option
import com.anadi.prabhupadalectures.datamodel.QueryParam

@Composable
fun FilterListItem(
    filter: Filter,
    modifier: Modifier = Modifier,
    onEvent: (CommonUiEvent.ResultsEvent) -> Unit = {}
) =
    Column(
        modifier = modifier.padding(vertical = 8.dp),
    ) {

        var isExpanded by remember { mutableStateOf(filter.isExpanded) }

        FilterTitle(filter, isExpanded) {
            isExpanded = it
            onEvent(CommonUiEvent.ResultsEvent.Expand(filter.name, it))
        }
        if (isExpanded) {
            filter.options.forEach { option ->
                OptionListItem(option) { isSelected ->
                    onEvent(
                        CommonUiEvent.ResultsEvent.Option(
                            QueryParam(
                                filterName = filter.name,
                                selectedOption = option.value,
                                isSelected = isSelected
                            )
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
            .padding(all = 12.dp)
    ) {
        Image(
            painter = painterResource(if (isExpanded) R.drawable.ic_minus else R.drawable.ic_plus),
            contentScale = ContentScale.FillBounds,
            contentDescription = "expand filter",
            modifier =
            Modifier
                .align(Alignment.CenterStart)
                .size(30.dp)
                .clickable { onExpandedChanged(!isExpanded) }
        )

        Text(
            text = filter.title,
            color = MaterialTheme.colors.onPrimary,
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
            .padding(all = 4.dp)

    ) {
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = option.text,
            color = MaterialTheme.colors.onSecondary,
            style = MaterialTheme.typography.h6,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1.0f)
        )
        Checkbox(
            checked = option.isSelected,
            onCheckedChange = { onOptionSelected?.invoke(it) },
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colors.onSecondary,
                checkmarkColor = MaterialTheme.colors.secondary,
            )
        )
        Spacer(modifier = Modifier.width(16.dp))
    }


@Preview
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