package com.anadi.prabhupadalectures.android.ui.compose

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.anadi.prabhupadalectures.android.R
import com.anadi.prabhupadalectures.android.ui.screens.results.ResultsEvent
import com.anadi.prabhupadalectures.data.lectures.Lecture
import com.anadi.prabhupadalectures.network.api.FULL_PROGRESS
import com.anadi.prabhupadalectures.network.api.ZERO_PROGRESS
import com.anadi.prabhupadalectures.repository.Pause
import com.anadi.prabhupadalectures.repository.Play

@Composable
fun LectureListItem(
    lecture: Lecture,
    isPlaying: Boolean,
    onEvent: (ResultsEvent) -> Unit = {},
) =
    Row(
        modifier = Modifier
            .padding(all = 4.dp)
            .padding(top = 12.dp),
        verticalAlignment = Alignment.Top
    ) {

        val playResId = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play

        Column(
            modifier =
            Modifier
                .align(CenterVertically)
                .weight(0.1f)
        ) {
            Image(
                painter = painterResource(playResId),
                contentScale = ContentScale.FillBounds,
                contentDescription = "play image",
                modifier =
                Modifier
                    .aspectRatio(1f)
                    .weight(0.1f)
                    .clickable { onEvent(ResultsEvent.Player(if (isPlaying) Pause else Play(lecture.id))) }
            )

            if (lecture.downloadProgress in ZERO_PROGRESS until FULL_PROGRESS) {
                Text(
                    text = "${lecture.downloadProgress}%",
                    maxLines = 1,
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 30.dp)
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
                    painter = painterResource(R.drawable.ic_download_progress),
                    contentScale = ContentScale.FillBounds,
                    contentDescription = "download progress image",
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(0.1f)
                        .alpha(alpha)
                )
            } else if (lecture.downloadProgress == FULL_PROGRESS) {
                Image(
                    painter = painterResource(R.drawable.ic_download_success),
                    contentScale = ContentScale.FillBounds,
                    contentDescription = "download success",
                    modifier = Modifier
                        .padding(top = 30.dp)
                        .aspectRatio(1f)
                        .weight(0.1f)
                )
            }
        }

        Spacer(modifier = Modifier.width(4.dp))

        // We toggle the isExpanded variable when we click on this Column
        Column(modifier = Modifier.weight(0.82f)) {
            Text(
                text = lecture.title,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.clickable { onEvent(ResultsEvent.Player(if (isPlaying) Pause else Play(lecture.id))) }
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = lecture.subTitle,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                color = GrayLight,
                style = MaterialTheme.typography.body1
            )
        }
        Spacer(modifier = Modifier.height(4.dp))

        val favoriteResId = if (lecture.isFavorite) R.drawable.ic_favorite else R.drawable.ic_non_favorite
        Image(
            painter = painterResource(favoriteResId),
            contentScale = ContentScale.FillBounds,
            contentDescription = "favorite image",
            modifier =
            Modifier
                .align(CenterVertically)
                .aspectRatio(1f)
                .weight(0.08f)
                .clickable { onEvent(ResultsEvent.Favorite(lecture, !lecture.isFavorite)) }
        )
    }

@Preview
@Composable
fun PreviewLectureListItem() {
    AppTheme {
        LectureListItem(
            getLecture(
                title = "Бхагавад-Гита. Вступление",
                date = "1970-08-02",
                place = "Лос-Анджелес, США"
            ),
            isPlaying = true
        )
    }
}

fun getLecture(title: String, date: String, place: String) =
    Lecture(
        title = title,
        date = date,
        place = place,
        durationMillis = 85_000,
        isFavorite = true
    )
