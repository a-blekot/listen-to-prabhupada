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
import com.anadi.prabhupadalectures.data.lectures.Lecture

@Composable
fun LectureListItem(
    lecture: Lecture,
    exoCallback: ((Lecture) -> Unit)? = null,
    onFavorite: ((Long, Boolean) -> Unit)? = null,
    modifier: Modifier = Modifier
) =
    Row(
        modifier = Modifier.padding(all = 4.dp).padding(top = 12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Image(
            painter = painterResource(R.drawable.play_img),
            contentScale = ContentScale.FillBounds,
            contentDescription = "play image",
            modifier =
                Modifier
                    .aspectRatio(1f)
                    .weight(0.1f)
                    .clickable { exoCallback?.invoke(lecture) }
        )

        Spacer(modifier = Modifier.width(4.dp))

        // We toggle the isExpanded variable when we click on this Column
        Column(modifier = Modifier.weight(0.8f)) {
            var isExpanded by remember { mutableStateOf(false) }
            Text(
                text = lecture.title,
                maxLines = if(isExpanded) Int.MAX_VALUE else 1,
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

        val favoriteResId = if (lecture.isFavorite) R.drawable.heart else R.drawable.heart_empty
        Image(
            painter = painterResource(favoriteResId),
            contentScale = ContentScale.FillBounds,
            contentDescription = "favorite image",
            modifier =
            Modifier
                .aspectRatio(1f)
                .weight(0.1f)
                .clickable { onFavorite?.invoke(lecture.id, !lecture.isFavorite) }
        )
    }

@Preview
@Composable
fun previewLectureListItem() {
    AppTheme {
        LectureListItem(
            getLecture(
                title = "Бхагавад-Гита. Вступление",
                date = "1970-08-02",
                place = "Лос-Анджелес, США",
                isFavorite = false
            )
        )
    }
}

fun getLecture(title: String, date: String, place: String, isFavorite: Boolean) =
    Lecture(
        title = title,
        date = date,
        place = place
    )
