package com.listentoprabhupada.android_ui.helpers

import android.media.session.PlaybackState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons.Rounded
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.listentoprabhupada.android_ui.LoadingBar
import com.listentoprabhupada.android_ui.custom.StandartRow
import com.listentoprabhupada.android_ui.theme.AppTheme
import com.listentoprabhupada.android_ui.theme.Colors.playerBg
import com.listentoprabhupada.android_ui.theme.Colors.playerButtons
import com.listentoprabhupada.android_ui.theme.Colors.playerDescr
import com.listentoprabhupada.android_ui.theme.Colors.playerTimeLineBg
import com.listentoprabhupada.android_ui.theme.Colors.playerTimeLineSelector
import com.listentoprabhupada.android_ui.theme.Colors.playerTimer
import com.listentoprabhupada.android_ui.theme.Colors.playerTitle
import com.listentoprabhupada.android_ui.theme.Dimens.bottomSheetPeekHeight
import com.listentoprabhupada.android_ui.theme.Dimens.paddingM
import com.listentoprabhupada.android_ui.theme.Dimens.paddingS
import com.listentoprabhupada.android_ui.theme.Dimens.paddingXS
import com.listentoprabhupada.android_ui.theme.Dimens.radiusXL
import com.listentoprabhupada.android_ui.utils.ONE_DAY_MS
import com.listentoprabhupada.android_ui.utils.formatTimeAdaptiveHoursMax
import com.listentoprabhupada.common.data.Lecture
import com.listentoprabhupada.common.player_api.PlayerComponent
import com.listentoprabhupada.common.player_api.PlayerState

@Composable
fun PlayerListItem(playerComponent: PlayerComponent, modifier: Modifier = Modifier) {
    val playbackState = playerComponent.flow.subscribeAsState()

    BoxWithConstraints(
        modifier = modifier
            .background(
                color = playerBg(),
                shape = RoundedCornerShape(topStart = radiusXL, topEnd = radiusXL)
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            SliderComposable(
                playbackState.value,
                playerComponent
            )

            Spacer(modifier = Modifier.height(paddingXS))

            MarqueeText(
                text = playbackState.value.lecture.title,
                color = playerTitle(),
                style = typography.titleMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(paddingXS))

            Text(
                text = playbackState.value.lecture.subTitle,
                color = playerDescr(),
                style = typography.bodyMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(paddingS))

            Row(
                modifier = Modifier.fillMaxWidth(0.7f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                PlayerActionIcon(Rounded.SkipPrevious, "previous") {
                    playerComponent.onPrev()
                }
                PlayerActionIcon(Rounded.Replay10, "seek back") {
                    playerComponent.onSeekBack()
                }
                val imageVector =
                    selector(Rounded.Pause, Rounded.PlayArrow, playbackState.value.isPlaying)
                PlayerActionIcon(imageVector, "play/pause", 1f) {
                    when (playbackState.value.isPlaying) {
                        true -> playerComponent.onPause()
                        else -> playerComponent.onPlay(playbackState.value.lecture.id)
                    }
                }
                PlayerActionIcon(Rounded.Forward10, "seek forward") {
                    playerComponent.onSeekForward()
                }
                PlayerActionIcon(Rounded.SkipNext, "next") {
                    playerComponent.onNext()
                }
            }
        }
        if (playbackState.value.isBuffering) {
            LoadingBar(
                Modifier
                    .align(Alignment.Center)
                    .fillMaxSize(),
                bgColor = Color.Transparent
            )
        }
    }
}

@Composable
fun RowScope.PlayerActionIcon(
    imageVector: ImageVector,
    description: String,
    weight: Float = 0.6f,
    aspectRatio: Float = 1f,
    onClick: () -> Unit
) =
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .aspectRatio(aspectRatio)
            .weight(weight)
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = description,
            tint = playerButtons(),
            modifier = Modifier.fillMaxSize().scale(1.0f)
        )
    }

@Composable
fun SliderComposable(playbackState: PlayerState, playerComponent: PlayerComponent) {

    Column(
        modifier = Modifier.fillMaxWidth().height(bottomSheetPeekHeight),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Divider(
            modifier = Modifier.fillMaxWidth(0.1f).padding(vertical = paddingS),
            color = playerTimeLineBg(),
            thickness = 2.dp
        )

        Slider(
            value = playbackState.timeMs.toFloat(),
            valueRange = 0f..playbackState.durationMs.coerceIn(1000L, ONE_DAY_MS).toFloat(),
            onValueChange = { playerComponent.onSeekTo(it.toLong()) },
            onValueChangeFinished = { playerComponent.onSliderReleased() },
            colors = SliderDefaults.colors(
                thumbColor = playerTimeLineSelector(),
                activeTrackColor = playerTimeLineBg()
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
        )

        StandartRow(
            modifier = Modifier.padding(horizontal = paddingS),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = playbackState.displayedTime,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = playerTimer(),
                style = typography.labelLarge,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.weight(1f))

            Text(
                text = playbackState.displayedTime,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = playerTimer(),
                style = typography.labelLarge,
                textAlign = TextAlign.Center
            )
        }
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
                override val flow: Value<PlayerState> =
                    MutableValue(getPlaybackState())
            }
        )
    }
}

fun getPlaybackState() =
    PlayerState(
        lecture = Lecture(
            id = 1L,
            title = "Бхагавад-гита 2.12",
            date = "9 марта, 1966",
            place = "Нью-Йорк (США)"
        ),
        isPlaying = true,
        timeMs = 30_000L,
        durationMs = 100_000L
    )
