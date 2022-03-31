package com.anadi.prabhupadalectures.android.ui.compose

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anadi.prabhupadalectures.android.R
import com.anadi.prabhupadalectures.android.player.SEEK_INCREMENT_MS
import com.anadi.prabhupadalectures.android.ui.screens.CommonUiEvent
import com.anadi.prabhupadalectures.android.ui.screens.results.LoadingBar
import com.anadi.prabhupadalectures.android.util.ONE_DAY_MS
import com.anadi.prabhupadalectures.android.util.formatTimeAdaptiveHoursMax
import com.anadi.prabhupadalectures.network.api.FULL_PROGRESS
import com.anadi.prabhupadalectures.repository.*

@Composable
fun PlayerListItem(
    playbackState: PlaybackState = PlaybackState(),
    onEvent: (CommonUiEvent) -> Unit = {},
) =
    Box(
        modifier = Modifier
            .padding(top = 12.dp)
            .background(
                color = MaterialTheme.colors.secondary,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(all = 4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            SliderComposable(
                playbackState,
                onEvent
            )

            Spacer(modifier = Modifier.height(20.dp))
            MarqueeText(
                text = playbackState.lecture.title,
//                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colors.onPrimary,
                fontSize = 18.sp,
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center
            )

            Text(
                text = playbackState.lecture.displayedDescription,
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
                PlayerActionIcon(R.drawable.ic_player_prev, "previous", onEvent, Prev, ratio = 1.4f)
                Spacer(modifier = Modifier.weight(0.1f))

                Box(
                    modifier = Modifier
                        .weight(0.1f)
                        .aspectRatio(1.1f)
                ) {
                    PlayerActionIcon(
                        R.drawable.ic_player_seek_backward,
                        "seek back",
                        onEvent,
                        SeekBack,
                        ratio = 1.1f
                    )
                    SeekText(Modifier.padding(start = 4.dp))
                }
                Spacer(modifier = Modifier.weight(0.1f))

                val playIconId =
                    if (playbackState.isPlaying) R.drawable.ic_player_pause else R.drawable.ic_player_play
                PlayerActionIcon(
                    playIconId,
                    "play/pause",
                    onEvent,
                    if (playbackState.isPlaying) Pause else Play(playbackState.lecture.id),
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
                        onEvent,
                        SeekForward,
                        ratio = 1.1f
                    )
                    SeekText(Modifier.padding(end = 4.dp))
                }
                Spacer(modifier = Modifier.weight(0.1f))

                PlayerActionIcon(R.drawable.ic_player_next, "next", onEvent, Next, ratio = 1.4f)
                Spacer(modifier = Modifier.weight(0.15f))
            }

            Spacer(modifier = Modifier.height(20.dp))

            when (playbackState.lecture.downloadProgress) {
                null ->
                    Text(
                        text = stringResource(R.string.download_lecture),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colors.onPrimary,
                        style = MaterialTheme.typography.body1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.clickable { onEvent(CommonUiEvent.Player(Download(playbackState.lecture))) }
                    )

                FULL_PROGRESS ->
                    Image(
                        painter = painterResource(R.drawable.ic_download_mark),
                        contentScale = ContentScale.FillBounds,
                        contentDescription = "download success image",
                        modifier = Modifier.size(30.dp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary)
                    )
                else ->
                    Row {
                        Text(
                            text = "${playbackState.lecture.downloadProgress}%",
                            maxLines = 1,
                            color = MaterialTheme.colors.onPrimary,
                            style = MaterialTheme.typography.body1,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        val infiniteTransition = rememberInfiniteTransition()
                        val alpha by infiniteTransition.animateFloat(
                            initialValue = 0f,
                            targetValue = 1f,
                            animationSpec = infiniteRepeatable(
                                animation = keyframes {
                                    durationMillis = 1000
                                    0.7f at 500
                                },
                                repeatMode = RepeatMode.Reverse
                            )
                        )
                        Image(
                            painter = painterResource(R.drawable.ic_notification_download_progress),
                            contentScale = ContentScale.FillBounds,
                            contentDescription = "download progress image",
                            modifier = Modifier
                                .size(30.dp)
                                .alpha(alpha)
                        )
                    }
            }
        }
        if (playbackState.isBuffering) {
            LoadingBar()
        }
    }

@Composable
fun RowScope.PlayerActionIcon(
    @DrawableRes id: Int,
    description: String,
    onEvent: (CommonUiEvent) -> Unit = {},
    playerAction: PlayerAction,
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
            .clickable { onEvent(CommonUiEvent.Player(playerAction)) }
    )

@Composable
fun BoxScope.PlayerActionIcon(
    @DrawableRes id: Int,
    description: String,
    onEvent: (CommonUiEvent) -> Unit = {},
    playerAction: PlayerAction,
    ratio: Float = 1f
) =
    Image(
        painter = painterResource(id),
        contentScale = ContentScale.FillBounds,
        contentDescription = description,
        modifier =
        Modifier
            .aspectRatio(ratio)
            .clickable { onEvent(CommonUiEvent.Player(playerAction)) }
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

@Composable
fun SliderComposable(playbackState: PlaybackState, onEvent: (CommonUiEvent) -> Unit = {}) {

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Slider(
            value = playbackState.timeMs.toFloat(),
            valueRange = 0f..playbackState.durationMs.coerceIn(1000L, ONE_DAY_MS).toFloat(),
            onValueChange = { onEvent(CommonUiEvent.Player(SeekTo(it.toLong()))) },
            onValueChangeFinished = { onEvent(CommonUiEvent.Player(SliderReleased)) },
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colors.onSurface,
                activeTrackColor = MaterialTheme.colors.primaryVariant
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = playbackState.displayedTime,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colors.onPrimary,
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center
        )
    }
}

val PlaybackState.displayedTime
    get() = "${formatTimeAdaptiveHoursMax(timeMs)} / ${formatTimeAdaptiveHoursMax(durationMs)}"

@Preview
@Composable
fun PreviewPlayerListItem() {
    AppTheme {
        PlayerListItem(
        )
    }
}

//fun getPlaybackState() =
//    PlaybackState(
//        lecture = Lecture(
//            id = 1L,
//            title = "Бхагавад-гита 2.12",
//            description = "1996.09.12 Mumbai"
//        ),
//        isPlaying = true,
//        timeMs = 10_000L,
//        durationMs = 100_000L
//    )
