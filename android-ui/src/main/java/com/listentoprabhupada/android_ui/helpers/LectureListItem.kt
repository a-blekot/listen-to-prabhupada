package com.listentoprabhupada.android_ui.helpers

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.listentoprabhupada.android_ui.custom.StandartRow
import com.listentoprabhupada.android_ui.theme.*
import com.listentoprabhupada.android_ui.theme.Colors.favSelected
import com.listentoprabhupada.android_ui.theme.Colors.favUnselected
import com.listentoprabhupada.android_ui.theme.Colors.lectureDescr
import com.listentoprabhupada.android_ui.theme.Colors.lecturePause
import com.listentoprabhupada.android_ui.theme.Colors.lecturePlay
import com.listentoprabhupada.android_ui.theme.Colors.lectureTitle
import com.listentoprabhupada.common.data.Lecture
import com.listentoprabhupada.common.data.LectureComponent

private const val FULL_PROGRESS = 100

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LectureListItem(lecture: Lecture, component: LectureComponent, modifier: Modifier = Modifier) =
    StandartRow(
        modifier = modifier.combinedClickable(
//                onLongClick = { showContextMenu(lecture, component) },
            onClick = {}
        ),
    ) {

        Image(
            imageVector = lecture.playIcon(),
            contentScale = ContentScale.FillBounds,
            contentDescription = "play image",
            colorFilter = ColorFilter.tint(lecture.playColor()),
            modifier =
            Modifier
                .weight(15f)
                .aspectRatio(1f)
                .clickable { component.togglePlay(lecture) }
        )

        Spacer(modifier = Modifier.weight(4f))

        Column(modifier = Modifier.weight(110f)) {
            Text(
                text = lecture.title,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                color = lectureTitle(),
                style = typography.titleSmall,
                modifier = Modifier
                    .combinedClickable(
//                        onLongClick = { showContextMenu(lecture, component) },
                        onClick = { component.togglePlay(lecture) }
                    )
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = lecture.subTitle,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                color = lectureDescr(),
                style = typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.weight(2f))

        Image(
            imageVector = lecture.favIcon(),
            contentScale = ContentScale.FillBounds,
            contentDescription = "favorite image",
            modifier =
            Modifier
                .weight(8f)
                .aspectRatio(1f)
                .clickable { component.onFavorite(lecture.id, !lecture.isFavorite) },
            colorFilter = ColorFilter.tint(favColor(lecture.isFavorite)),
        )
    }

@Composable
fun Lecture.playIcon() =
    when {
        isPlaying -> Icons.Rounded.PauseCircleOutline
        isCompleted -> Icons.Rounded.TaskAlt
        else -> Icons.Rounded.PlayCircleOutline
    }

@Composable
fun Lecture.playColor() =
    when {
        isPlaying -> lecturePlay()
        else -> lecturePause()
    }

@Composable
fun Lecture.favIcon() =
    when {
        isFavorite -> Icons.Rounded.Favorite
        else -> Icons.Rounded.FavoriteBorder
    }

@Composable
private fun favColor(isFavorite: Boolean) =
    if (isFavorite) favSelected() else favUnselected()

private fun LectureComponent.togglePlay(lecture: Lecture) =
    if (lecture.isPlaying) onPause() else onPlay(lecture.id)

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
            object : LectureComponent {}
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
