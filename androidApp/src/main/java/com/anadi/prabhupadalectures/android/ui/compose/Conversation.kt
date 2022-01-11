package com.anadi.prabhupadalectures.android.ui.compose

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.anadi.prabhupadalectures.network.api.dumy.UserPreview

@Composable
fun Users(users: List<UserPreview>, callback: () -> Unit) {
    LazyColumn {
        items(users) { user ->
            UserCard(user) {
                callback()
            }
        }
    }
}

@Composable
fun Conversation(messages: List<Message>) {
    LazyColumn {
        items(messages) { message ->
            MessageCard(message)
        }
    }
}

@Preview
@Composable
fun PreviewConversation() {
    AppTheme {
        Conversation(conversationSample)
    }
}

val conversationSample = listOf(
    Message("Prabhupad", "Hare Krishna, take a look at Jetpack Compose, it's great!"),
    Message("Prabhupad", "Hare Krishna, take a look at Jetpack Compose, it's great!"),
    Message("Prabhupad", "Hare Krishna, take a look at Jetpack Compose, it's great!"),
    Message("Prabhupad", "Hare Krishna, take a look at Jetpack Compose, it's great!"),
    Message("Prabhupad", "Hare Krishna, take a look at Jetpack Compose, it's great!"),
    Message("Prabhupad", "Hare Krishna, take a look at Jetpack Compose, it's great!"),
    Message("Prabhupad", "Hare Krishna, take a look at Jetpack Compose, it's great!"),
    Message("Prabhupad", "Hare Krishna, take a look at Jetpack Compose, it's great!"),
    Message("Prabhupad", "Hare Krishna, take a look at Jetpack Compose, it's great!"),
    Message("Prabhupad", "Hare Krishna, take a look at Jetpack Compose, it's great!"),
    Message("Prabhupad", "Hare Krishna, take a look at Jetpack Compose, it's great!"),
    Message("Prabhupad", "Hare Krishna, take a look at Jetpack Compose, it's great!"),
    Message("Prabhupad", "Hare Krishna, take a look at Jetpack Compose, it's great!"),
    Message("Prabhupad", "Hare Krishna, take a look at Jetpack Compose, it's great!"),
    Message("Prabhupad", "Hare Krishna, take a look at Jetpack Compose, it's great!"),
    Message("Prabhupad", "Hare Krishna, take a look at Jetpack Compose, it's great!"),
    Message("Prabhupad", "Hare Krishna, take a look at Jetpack Compose, it's great!"),
    Message("Prabhupad", "Hare Krishna, take a look at Jetpack Compose, it's great!"),
    Message("Prabhupad", "Hare Krishna, take a look at Jetpack Compose, it's great!"),
    Message("Prabhupad", "Hare Krishna, take a look at Jetpack Compose, it's great!"),
    Message("Prabhupad", "Hare Krishna, take a look at Jetpack Compose, it's great!"),
    Message("Prabhupad", "Hare Krishna, take a look at Jetpack Compose, it's great!")
)