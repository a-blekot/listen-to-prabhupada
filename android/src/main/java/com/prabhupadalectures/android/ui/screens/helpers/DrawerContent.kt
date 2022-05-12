package com.prabhupadalectures.android.ui.screens.helpers

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.prabhupadalectures.android.R
import com.prabhupadalectures.common.feature_results_api.ResultsComponent

@Composable
fun DrawerContent(component: ResultsComponent) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primaryVariant)
            .padding(top = 60.dp, start = 30.dp)
    ) {

        Text(
            text = stringResource(R.string.favorites),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colors.onPrimary,
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.Start)
                .clickable { component.onShowFavorites() }
        )

        Text(
            text = stringResource(R.string.downloads),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colors.onPrimary,
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(top = 20.dp)
                .clickable { component.onShowDownloads() }
        )
    }
}