package com.listentoprabhupada.android_ui.helpers

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.listentoprabhupada.common.data.Lecture

private const val FULL_PROGRESS = 100

interface Listener {
    fun onPause() {}
    fun onPlay(id: Long) {}
    fun onFavorite(id: Long, isFavorite: Boolean) {}
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LectureListItem(
    lecture: Lecture,
    component: Listener,
) =
    Row(
        modifier = Modifier
            .combinedClickable(
//                onLongClick = { showContextMenu(lecture, component) },
                onClick = {}
            ),
    ) {

        val playVector =
            when {
                lecture.isPlaying -> Icons.Rounded.PauseCircleOutline
                lecture.isCompleted -> Icons.Rounded.TaskAlt
                else -> Icons.Rounded.PlayCircleOutline
            }

        Image(
            imageVector = playVector,
            contentScale = ContentScale.FillBounds,
            contentDescription = "play image",
            colorFilter = ColorFilter.tint(BrownMedLight2),
            modifier =
            Modifier
                .align(CenterVertically)
                .weight(15f)
                .aspectRatio(1f)
                .clickable {
                    if (lecture.isPlaying) {
                        component.onPause()
                    } else {
                        component.onPlay(lecture.id)
                    }
                }
        )

        Spacer(modifier = Modifier.weight(4f))

        // We toggle the isExpanded variable when we click on this Column
        Column(
            modifier = Modifier
                .weight(110f)
        ) {
            Text(
                text = lecture.title,
                fontSize = 17.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .combinedClickable(
//                        onLongClick = { showContextMenu(lecture, component) },
                        onClick = {
                            if (lecture.isPlaying) {
                                component.onPause()
                            } else {
                                component.onPlay(lecture.id)
                            }
                        }
                    )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = lecture.subTitle,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                color = GrayLight,
                style = MaterialTheme.typography.titleSmall
            )
        }

        Spacer(modifier = Modifier.weight(2f))

        Image(
//                painter = if (lecture.isFavorite) R.drawable.ic_star else R.drawable.ic_star_border,
            imageVector = if (lecture.isFavorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
            contentScale = ContentScale.FillBounds,
            contentDescription = "favorite image",
            modifier =
            Modifier
                .weight(8f)
                .aspectRatio(1f)
                .clickable { component.onFavorite(lecture.id, !lecture.isFavorite) },
            colorFilter = ColorFilter.tint(if (lecture.isFavorite) favoriteSelected else favoriteUnselected),
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
            object : Listener {}
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
