package com.prabhupadalectures.android.ui.screens.helpers

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.prabhupadalectures.android.R


@Composable
fun Header(
    totalLectures: Int = 3703,
    needsTranslate: Int = 843,
    modifier: Modifier = Modifier
) =
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {

        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary),
            contentDescription = "logo image",
            modifier = Modifier
                .fillMaxWidth(0.2f)
                .align(Alignment.CenterHorizontally)
                .aspectRatio(1f)
        )

        Text(
            text = stringResource(id = R.string.header_app_name),
            style = MaterialTheme.typography.h5,
            maxLines = 1,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colors.onSecondary,
            modifier = Modifier.padding(bottom = 40.dp)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(id = R.string.header_archive),
                style = MaterialTheme.typography.h4,
                maxLines = 1,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colors.onSurface,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 10.dp)
            )
            Text(
                text = stringResource(id = R.string.header_lectures),
                maxLines = 1,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.h4,
                color = MaterialTheme.colors.onSecondary,
                fontWeight = FontWeight.Bold
            )
        }

        Text(
            text = stringResource(id = R.string.header_of_srila_prabhupada),
            style = MaterialTheme.typography.h4,
            color = MaterialTheme.colors.onSecondary,
            maxLines = 1,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 30.dp)
        )

        Text(
            text = stringResource(id = R.string.header_of_total_audio, "$totalLectures"),
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.onSecondary,
            maxLines = 1,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        Text(
            text = stringResource(id = R.string.header_of_needs_translation, "$needsTranslate"),
            style = MaterialTheme.typography.h6,
            color = MaterialTheme.colors.onSecondary,
            maxLines = 1,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        Text(
            text = stringResource(id = R.string.header_help_with_translation),
            style = MaterialTheme.typography.h6.copy(
                textDecoration = TextDecoration.Underline
            ),
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colors.primary,
            modifier = Modifier
                .padding(bottom = 20.dp)
                .clickable { /**onEvent(CommonUiEvent.ResultsEvent.OpenHelpTranslation) **/ }
        )
    }