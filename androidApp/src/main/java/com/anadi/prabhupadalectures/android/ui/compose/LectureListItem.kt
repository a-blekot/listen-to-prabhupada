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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.anadi.prabhupadalectures.android.R
import com.anadi.prabhupadalectures.data.lectures.FileInfo
import com.anadi.prabhupadalectures.data.lectures.Lecture

@Composable
fun LectureListItem(
    lecture: Lecture,
    isPlaying: Boolean,
    uiListener: ((UIAction) -> Unit)? = null
) =
    Row(
        modifier = Modifier
            .padding(all = 4.dp)
            .padding(top = 12.dp),
        verticalAlignment = Alignment.Top
    ) {
        val playResId = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play
        Image(
            painter = painterResource(playResId),
            contentScale = ContentScale.FillBounds,
            contentDescription = "play image",
            modifier =
            Modifier
                .aspectRatio(1f)
                .weight(0.1f)
                .clickable { uiListener?.invoke(if (isPlaying) Pause else Play(lecture.id)) }
        )

        Spacer(modifier = Modifier.width(4.dp))

        // We toggle the isExpanded variable when we click on this Column
        Column(modifier = Modifier.weight(0.82f)) {
            var isExpanded by remember { mutableStateOf(false) }
            Text(
                text = lecture.title,
                maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                overflow = if (isExpanded) TextOverflow.Visible else TextOverflow.Ellipsis,
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.clickable { isExpanded = !isExpanded }
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = lecture.displayedSubTitle,
                maxLines = 1,
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
                .aspectRatio(1f)
                .weight(0.08f)
                .clickable { uiListener?.invoke(Favorite(lecture.id, !lecture.isFavorite)) }
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
        fileInfo = FileInfo(duration = "05:25"),
        isFavorite = true
    )
