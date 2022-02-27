package com.anadi.prabhupadalectures.android.ui.compose

import androidx.annotation.DrawableRes
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
import com.anadi.prabhupadalectures.android.player.SEEK_INCREMENT_MS
import com.anadi.prabhupadalectures.android.util.formatTimeAdaptiveHoursMax
import com.anadi.prabhupadalectures.data.lectures.Lecture
import com.anadi.prabhupadalectures.repository.PlaybackState

@Composable
fun PlayerListItem(
    playbackState: PlaybackState,
    uiListener: ((UIAction) -> Unit)? = null
) =
    Column(
        modifier = Modifier
            .padding(all = 4.dp)
            .padding(top = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = playbackState.displayedTime,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colors.onPrimary,
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = playbackState.title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colors.onPrimary,
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Center
        )

        Text(
            text = playbackState.description,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = GrayLight,
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .height(28.dp)
                .padding(start = 20.dp)
                .padding(end = 20.dp)
                .padding(bottom = 30.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.weight(0.15f))
            PlayerActionIcon(R.drawable.ic_player_prev, "previous", uiListener, Prev, ratio = 1.4f)
            Spacer(modifier = Modifier.weight(0.1f))

            Box(
                modifier = Modifier
                    .weight(0.1f)
                    .aspectRatio(1.1f)
            ) {
                PlayerActionIcon(
                    R.drawable.ic_player_seek_backward,
                    "seek back",
                    uiListener,
                    SeekBack,
                    ratio = 1.1f
                )
                SeekText(Modifier.padding(start = 4.dp))
            }
            Spacer(modifier = Modifier.weight(0.1f))

            val playIconId = if (playbackState.isPlaying) R.drawable.ic_player_play else R.drawable.ic_player_pause
            PlayerActionIcon(
                playIconId,
                "play/pause",
                uiListener,
                Play(Lecture(), !playbackState.isPlaying),
                ratio = 0.9f
            )
            Spacer(modifier = Modifier.weight(0.1f))

            Box(
                modifier = Modifier
                    .weight(0.1f)
                    .aspectRatio(1.1f)
            ) {
                PlayerActionIcon(
                    R.drawable.ic_player_seek_forward,
                    "seek forward",
                    uiListener,
                    SeekForward,
                    ratio = 1.1f
                )
                SeekText(Modifier.padding(end = 4.dp))
            }
            Spacer(modifier = Modifier.weight(0.1f))

            PlayerActionIcon(R.drawable.ic_player_next, "next", uiListener, Next, ratio = 1.4f)
            Spacer(modifier = Modifier.weight(0.15f))
        }
    }

@Composable
fun RowScope.PlayerActionIcon(
    @DrawableRes id: Int,
    description: String,
    uiListener: ((UIAction) -> Unit)? = null,
    uiAction: UIAction,
    ratio: Float = 1f
) =
    Image(
        painter = painterResource(id),
        contentScale = ContentScale.FillBounds,
        contentDescription = description,
        modifier =
        Modifier
            .aspectRatio(ratio)
            .weight(0.1f)
            .clickable { uiListener?.invoke(uiAction) }
    )

@Composable
fun BoxScope.PlayerActionIcon(
    @DrawableRes id: Int,
    description: String,
    uiListener: ((UIAction) -> Unit)? = null,
    uiAction: UIAction,
    ratio: Float = 1f
) =
    Image(
        painter = painterResource(id),
        contentScale = ContentScale.FillBounds,
        contentDescription = description,
        modifier =
        Modifier
            .aspectRatio(ratio)
            .clickable { uiListener?.invoke(uiAction) }
    )

@Composable
fun BoxScope.SeekText(modifier: Modifier = Modifier) =
    Text(
        text = "${SEEK_INCREMENT_MS / 1000}",
        maxLines = 1,
        color = MaterialTheme.colors.onPrimary,
        style = MaterialTheme.typography.body2,
        textAlign = TextAlign.Center,
        modifier = modifier.align(Alignment.Center)
    )

val PlaybackState.displayedTime
    get() = "${formatTimeAdaptiveHoursMax(timeMs)} / ${formatTimeAdaptiveHoursMax(durationMs)}"

@Preview
@Composable
fun PreviewPlayerListItem() {
    AppTheme {
        PlayerListItem(
            getPlaybackState()
        )
    }
}

fun getPlaybackState() =
    PlaybackState(
        lectureId = 1L,
        title = "Бхагавад-гита 2.12",
        description = "1996.09.12 Mumbai",
        isPlaying = true,
        timeMs = 10_000L,
        durationMs = 100_000L
    )
