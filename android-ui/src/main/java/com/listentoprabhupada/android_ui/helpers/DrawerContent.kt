package com.listentoprabhupada.android_ui.helpers

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.listentoprabhupada.android_ui.R
import com.listentoprabhupada.android_ui.theme.Dimens.paddingL
import com.listentoprabhupada.common.feature_results_api.ResultsComponent

@Composable
fun DrawerContent(component: ResultsComponent) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 60.dp, start = 30.dp),
        verticalArrangement = Arrangement.spacedBy(paddingL)
    ) {

        Text(
            text = stringResource(R.string.favorites),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .clickable { component.onShowFavorites() }
        )

        Text(
            text = stringResource(R.string.downloads),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .clickable { component.onShowDownloads() }
        )
    }
}