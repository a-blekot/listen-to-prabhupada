package com.anadi.prabhupadalectures.android.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.anadi.prabhupadalectures.android.R

@Composable
fun OfflineComposable() =
    Column(
        Modifier
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
            text = stringResource(R.string.offline_text),
            color = MaterialTheme.colors.onPrimary,
            style = MaterialTheme.typography.h3,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 30.dp)
        )
    }