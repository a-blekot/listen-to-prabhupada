package com.anadi.prabhupadalectures.android.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.anadi.prabhupadalectures.android.R
import com.anadi.prabhupadalectures.events.CommonUiEvent

@Composable
fun OfflineComposable(onEvent: (CommonUiEvent) -> Unit = {}) =
    Column(
        Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.surface)
    ) {
        Image(
            painter = painterResource(R.drawable.prabhupada_offline),
            contentScale = ContentScale.FillBounds,
            contentDescription = "offline image",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.37f)
                .align(Alignment.CenterHorizontally)
                .padding(top = 30.dp)
        )

        Text(
            text = stringResource(R.string.no_internet_connection),
            color = MaterialTheme.colors.onPrimary,
            style = MaterialTheme.typography.h3,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 30.dp)
        )

        Text(
            text = stringResource(R.string.see_downloads),
            //            style = MaterialTheme.typography.h1,
            style = TextStyle(
                textDecoration = TextDecoration.Underline,
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colors.primary,
            textAlign = TextAlign.Center,

            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 30.dp)
                .clickable { onEvent(CommonUiEvent.ResultsEvent.OpenDownloads) }
        )
    }