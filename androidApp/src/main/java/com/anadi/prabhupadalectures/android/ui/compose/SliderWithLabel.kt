package com.anadi.prabhupadalectures.android.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.anadi.prabhupadalectures.android.util.formatTimeAdaptiveHoursMax

@Composable
fun SliderWithLabel(
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    labelMinWidth: Dp = 24.dp,
    onValueChange: ((Float) -> Unit)? = null,
    onValueChangeFinished: (() -> Unit)? = null
) {
    var labelVisibility by remember { mutableStateOf(false) }
    var sliderPos by remember { mutableStateOf(value) }

    Column {

        BoxWithConstraints(
            modifier = Modifier.fillMaxWidth()
        ) {
            val offset = getSliderOffset(
                value = sliderPos,
                valueRange = valueRange,
                boxWidth = maxWidth,
                labelWidth = labelMinWidth + 8.dp // Since we use a padding of 4.dp on either sides of the SliderLabel, we need to account for this in our calculation
            )

            if (labelVisibility) {
                SliderLabel(
                    label = formatTimeAdaptiveHoursMax(sliderPos.toLong()),
                    minWidth = labelMinWidth,
                    modifier = Modifier.padding(start = offset)
                )
            }
        }

        Slider(
            value = value,
            onValueChange = {
                labelVisibility = true
                sliderPos = it
                onValueChange?.invoke(it)
            },
            onValueChangeFinished = {
                labelVisibility = false
                onValueChangeFinished?.invoke()
            },
            valueRange = valueRange,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colors.onSurface,
                activeTrackColor = MaterialTheme.colors.primaryVariant
            ),
            modifier = Modifier.fillMaxWidth()
        )

    }
}


@Composable
fun SliderLabel(label: String, minWidth: Dp, modifier: Modifier = Modifier) {
    Text(
        label,
        textAlign = TextAlign.Center,
        color = Color.White,
        modifier = modifier
            .background(
                color = MaterialTheme.colors.primary,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(4.dp)
            .defaultMinSize(minWidth = minWidth)
    )
}


private fun getSliderOffset(
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    boxWidth: Dp,
    labelWidth: Dp
): Dp {

    val coerced = value.coerceIn(valueRange.start, valueRange.endInclusive)
    val positionFraction = calcFraction(valueRange.start, valueRange.endInclusive, coerced)

    return (boxWidth - labelWidth) * positionFraction
}


// Calculate the 0..1 fraction that `pos` value represents between `a` and `b`
private fun calcFraction(a: Float, b: Float, pos: Float) =
    (if (b - a == 0f) 0f else (pos - a) / (b - a)).coerceIn(0f, 1f)