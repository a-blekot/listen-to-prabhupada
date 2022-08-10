package com.listentoprabhupada.android_ui.helpers

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.listentoprabhupada.android_ui.R
import com.listentoprabhupada.android_ui.theme.Colors.primary
import com.listentoprabhupada.android_ui.theme.Colors.tertiary


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
            colorFilter = ColorFilter.tint(tertiary()),
            contentDescription = "logo image",
            modifier = Modifier
                .fillMaxWidth(0.2f)
                .align(Alignment.CenterHorizontally)
                .aspectRatio(1f)
        )

        Text(
            text = stringResource(id = R.string.header_app_name),
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            color = tertiary(),
            modifier = Modifier.padding(bottom = 40.dp)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(id = R.string.header_archive),
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                color = primary(),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 10.dp)
            )
            Text(
                text = stringResource(id = R.string.header_lectures),
                maxLines = 1,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleLarge,
                color = tertiary(),
                fontWeight = FontWeight.Bold
            )
        }

        Text(
            text = stringResource(id = R.string.header_of_srila_prabhupada),
            style = MaterialTheme.typography.titleLarge,
            color = tertiary(),
            maxLines = 1,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 30.dp)
        )

        Text(
            text = stringResource(id = R.string.header_of_total_audio, "$totalLectures"),
            style = MaterialTheme.typography.titleLarge,
            color = tertiary(),
            maxLines = 1,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        Text(
            text = stringResource(id = R.string.header_of_needs_translation, "$needsTranslate"),
            style = MaterialTheme.typography.titleLarge,
            color = tertiary(),
            maxLines = 1,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        Text(
            text = stringResource(id = R.string.header_help_with_translation),
            style = MaterialTheme.typography.titleLarge.copy(
                textDecoration = TextDecoration.Underline
            ),
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = primary(),
            modifier = Modifier
                .padding(bottom = 20.dp)
                .clickable { /**onEvent(CommonUiEvent.ResultsEvent.OpenHelpTranslation) **/ }
        )
    }