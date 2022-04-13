package com.anadi.prabhupadalectures.android.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.Top
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anadi.prabhupadalectures.android.R
import com.anadi.prabhupadalectures.events.CommonUiEvent
import com.anadi.prabhupadalectures.data.lectures.Lecture
import com.anadi.prabhupadalectures.network.api.FULL_PROGRESS
import com.anadi.prabhupadalectures.events.Pause
import com.anadi.prabhupadalectures.events.Play

@Composable
fun LectureListItem(
    lecture: Lecture,
    isPlaying: Boolean,
    onEvent: (CommonUiEvent) -> Unit = {},
) =
    Row(
        modifier = Modifier.padding(top = 8.dp),
    ) {

        val testBgAlpha = 0

        val playResId =
            when {
                isPlaying -> R.drawable.ic_pause2
                lecture.isCompleted -> R.drawable.ic_heard_mark
                else -> R.drawable.ic_play
            }

        Image(
            painter = painterResource(playResId),
            contentScale = ContentScale.FillBounds,
            contentDescription = "play image",
            modifier =
            Modifier
                .align(CenterVertically)
                .weight(15f)
                .aspectRatio(1f)
                .background(Color(130, 0, 255, testBgAlpha))
                .clickable { onEvent(CommonUiEvent.Player(if (isPlaying) Pause else Play(lecture.id))) }
        )

        Spacer(modifier = Modifier.weight(4f))

        // We toggle the isExpanded variable when we click on this Column
        Column(
            modifier = Modifier
                .weight(110f)
                .background(Color(130, 0, 255, testBgAlpha))
        ) {
            Text(
                text = lecture.title,
                fontSize = 17.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.clickable { onEvent(CommonUiEvent.Player(if (isPlaying) Pause else Play(lecture.id))) }
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

        Spacer(modifier = Modifier.weight(2f))

        Column(
            modifier = Modifier
                .weight(8f)
                .background(Color(130, 0, 255, testBgAlpha))
                .align(Top)
        ) {

            val favoriteResId = if (lecture.isFavorite) R.drawable.ic_star else R.drawable.ic_star_border
            Image(
                painter = painterResource(favoriteResId),
                contentScale = ContentScale.FillBounds,
                contentDescription = "favorite image",
                modifier =
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clickable { onEvent(CommonUiEvent.Favorite(lecture, !lecture.isFavorite)) }
            )

            if (lecture.downloadProgress == FULL_PROGRESS) {
                Image(
                    painter = painterResource(R.drawable.ic_download_mark),
                    contentScale = ContentScale.FillBounds,
                    contentDescription = "download success",
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                        .aspectRatio(1f)
                )
            }
        }

        Spacer(modifier = Modifier.weight(2f))

        Image(
            painter = painterResource(R.drawable.ic_context_menu),
            contentScale = ContentScale.FillBounds,
            contentDescription = "menu image",
            modifier =
            Modifier
                .align(CenterVertically)
                .weight(5f)
                .background(Color(130, 0, 255, testBgAlpha))
                .aspectRatio(0.4f)
                .clickable { onEvent(CommonUiEvent.Share(lecture.id)) }
        )
    }

@Preview(widthDp = 360, heightDp = 40)
@Composable
fun PreviewLectureListItem() {
    AppTheme {
        LectureListItem(
            getLecture(
                title = "Бхагавад-Гита. Вступление. Беседа на утренней прогулке",
                date = "1970-08-02",
                place = "Лос-Анджелес, США"
            ),
            isPlaying = false
        )
    }
}

fun getLecture(title: String, date: String, place: String) =
    Lecture(
        title = title,
        date = date,
        place = place,
        durationMillis = 85_000,
        isFavorite = true,
        isCompleted = false,
        downloadProgress = FULL_PROGRESS
    )
