package com.anadi.prabhupadalectures.android.ui.compose

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.anadi.prabhupadalectures.network.api.dumy.UserPreview

@Composable
fun UserCard(user: UserPreview, callback: () -> Unit) {
    // Add padding around our message
    Row(
        modifier = Modifier.padding(all = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberImagePainter(user.picture),
            contentDescription = "UserPreview picture",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colors.primary, CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))

        // We keep track if the message is expanded or not in this
        // variable
        var isExpanded by remember { mutableStateOf(false) }
        // surfaceColor will be updated gradually from one color to the other
        val surfaceColor: Color by animateColorAsState(
            if (isExpanded) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
        )

        // We toggle the isExpanded variable when we click on this Column
        Column(modifier = Modifier.clickable {
            isExpanded = !isExpanded
            if (isExpanded) {
                callback()
            }
        }) {
            Text(
                text = user.id,
                color = MaterialTheme.colors.primaryVariant,
                style = MaterialTheme.typography.subtitle1
            )
            // Add a vertical space between the author and message texts
            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                shape = MaterialTheme.shapes.medium,
                elevation = 1.dp,
                // surfaceColor color will be changing gradually from primary to surface
                color = surfaceColor,
                // animateContentSize will change the Surface size gradually
                modifier = Modifier.animateContentSize().padding(1.dp)
            ) {
                Text(
                    text = user.fullName,
                    modifier = Modifier.padding(all = 4.dp),
                    // If the message is expanded, we display all its content
                    // otherwise we only display the first line
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }
}