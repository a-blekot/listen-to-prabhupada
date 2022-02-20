package com.anadi.prabhupadalectures.android.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.anadi.prabhupadalectures.data.lectures.Lecture
import com.anadi.prabhupadalectures.datamodel.Playlist

@Composable
fun PlayerListItem(
    playlist: Playlist,
    uiListener: ((UIAction) -> Unit)? = null
) =
    Column(
        modifier = Modifier
            .padding(all = 4.dp)
            .padding(top = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = playlist.currentLecture?.fileInfo?.duration ?: "",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colors.onPrimary,
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = playlist.currentLecture?.displayedTitle ?: "",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colors.onPrimary,
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Center
        )

        Text(
            text = playlist.currentLecture?.displayedSubTitle ?: "",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = GrayLight,
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Image(
            painter = painterResource(R.drawable.ic_play),
            contentScale = ContentScale.FillBounds,
            contentDescription = "play image",
            modifier =
            Modifier
                .aspectRatio(1f)
                .weight(0.1f)
                .clickable { uiListener?.invoke(PlayClick(Lecture(), true)) }
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = playlist.currentLecture?.displayedSubTitle ?: "",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = GrayLight,
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center
        )
    }

@Preview
@Composable
fun PreviewPlayerListItem() {
    AppTheme {
        PlayerListItem(
            getPlaylist()
        )
    }
}

fun getPlaylist() =
    Playlist(
        lectures = listOf(
            getLecture("Бхагавад-гита 2.12", "1996.09.12", "Mumbai"),
            getLecture("lecture one", "1996.09.12", "Mumbai"),
            getLecture("lecture one", "1996.09.12", "Mumbai"),
        )
    )

