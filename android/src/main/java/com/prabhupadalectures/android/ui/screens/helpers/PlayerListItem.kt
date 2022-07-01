package com.prabhupadalectures.android.ui.screens.helpers

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
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
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.prabhupadalectures.android.PrabhupadaApp.Companion.app
import com.prabhupadalectures.android.R
import com.prabhupadalectures.android.player.SEEK_INCREMENT_MS
import com.prabhupadalectures.android.ui.LoadingBar
import com.prabhupadalectures.android.util.ONE_DAY_MS
import com.prabhupadalectures.android.util.formatTimeAdaptiveHoursMax
import com.prabhupadalectures.common.network_api.FULL_PROGRESS
import com.prabhupadalectures.common.player_api.PlayerComponent
import com.prabhupadalectures.common.player_api.PlayerState

@Composable
fun PlayerListItem(playerComponent: PlayerComponent) {
    val playbackState = playerComponent.flow.subscribeAsState()

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
                playbackState.value,
                playerComponent
            )

            Spacer(modifier = Modifier.height(20.dp))
            MarqueeText(
                text = playbackState.value.lecture.title,
//                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colors.onPrimary,
                fontSize = 18.sp,
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center
            )

            Text(
                text = playbackState.value.lecture.displayedDescription,
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
                PlayerActionIcon(R.drawable.ic_player_prev, "previous", 1.4f) {
                    playerComponent.onPrev()
                }
                Spacer(modifier = Modifier.weight(0.1f))

                Box(
                    modifier = Modifier
                        .weight(0.1f)
                        .aspectRatio(1.1f)
                ) {
                    PlayerActionIcon(R.drawable.ic_player_seek_backward, "seek back", 1.1f) {
                        playerComponent.onSeekBack()
                    }
                    SeekText(Modifier.padding(start = 4.dp))
                }
                Spacer(modifier = Modifier.weight(0.1f))

                val playIconId =
                    if (playbackState.value.isPlaying) R.drawable.ic_player_pause else R.drawable.ic_player_play
                PlayerActionIcon(playIconId, "play/pause", 0.9f) {
                    when (playbackState.value.isPlaying) {
                        true -> playerComponent.onPause()
                        else -> playerComponent.onPlay(playbackState.value.lecture.id)
                    }
                }
                Spacer(modifier = Modifier.weight(0.1f))

                Box(
                    modifier = Modifier
                        .weight(0.1f)
                        .aspectRatio(1.1f)
                ) {
                    PlayerActionIcon(R.drawable.ic_player_seek_forward, "seek forward", 1.1f) {
                        playerComponent.onSeekForward()
                    }
                    SeekText(Modifier.padding(end = 4.dp))
                }
                Spacer(modifier = Modifier.weight(0.1f))

                PlayerActionIcon(R.drawable.ic_player_next, "next", 1.4f) {
                    playerComponent.onNext()
                }
                Spacer(modifier = Modifier.weight(0.15f))
            }

            Spacer(modifier = Modifier.height(20.dp))

            var expanded by remember { mutableStateOf(false) }

            when (playbackState.value.lecture.downloadProgress) {
                null ->
                    Text(
                        text = stringResource(R.string.download_lecture),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colors.onPrimary,
                        style = MaterialTheme.typography.body1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.clickable {
                            expanded = !expanded
                            app.downloadsRepository.download(playbackState.value.lecture)
//                            playerComponent.onDownload(playbackState.value.lecture)
                        }
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
                            text = "${playbackState.value.lecture.downloadProgress}%",
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

//            Spacer(modifier = Modifier.height(10.dp))

//
//            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
//                list.forEach {
//                    DropdownMenuItem(
//                        onClick = {
//                            expanded = false
//                            playerComponent.onSpeed(it.speed)
//                        }
//                    ) {
//                        Text(text = it.title)
//                    }
//                }
//            }
        }
        if (playbackState.value.isBuffering) {
            LoadingBar()
        }
    }
}

private val list = listOf(
    Speedy("0,50", 0.50f),
    Speedy("0,75", 0.75f),
    Speedy("1,00", 1.00f),
    Speedy("1,25", 1.25f),
    Speedy("1,50", 1.50f),
    Speedy("1,75", 1.75f),
    Speedy("2,00", 2.00f),
)

private class Speedy(val title: String, val speed: Float)

@Composable
fun RowScope.PlayerActionIcon(
    @DrawableRes id: Int,
    description: String,
    ratio: Float = 1f,
    onClick: () -> Unit
) =
    Image(
        painter = painterResource(id),
        contentScale = ContentScale.FillBounds,
        contentDescription = description,
        modifier =
        Modifier
            .aspectRatio(ratio)
            .weight(0.1f)
            .clickable { onClick() }
    )

@Composable
fun BoxScope.PlayerActionIcon(
    @DrawableRes id: Int,
    description: String,
    ratio: Float = 1f,
    onClick: () -> Unit
) =
    Image(
        painter = painterResource(id),
        contentScale = ContentScale.FillBounds,
        contentDescription = description,
        modifier =
        Modifier
            .aspectRatio(ratio)
            .clickable { onClick() }
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
fun SliderComposable(playbackState: PlayerState, playerComponent: PlayerComponent) {

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Slider(
            value = playbackState.timeMs.toFloat(),
            valueRange = 0f..playbackState.durationMs.coerceIn(1000L, ONE_DAY_MS).toFloat(),
            onValueChange = { playerComponent.onSeekTo(it.toLong()) },
            onValueChangeFinished = { playerComponent.onSliderReleased() },
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

val PlayerState.displayedTime
    get() = "${formatTimeAdaptiveHoursMax(timeMs)} / ${formatTimeAdaptiveHoursMax(durationMs)}"

@Preview
@Composable
fun PreviewPlayerListItem() {
    AppTheme {
        PlayerListItem(
            object : PlayerComponent {
                override val flow: Value<PlayerState> = MutableValue(PlayerState())
            }
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
