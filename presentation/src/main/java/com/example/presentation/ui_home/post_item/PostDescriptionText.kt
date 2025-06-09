package com.example.presentation.ui_home.post_item

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.R
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PostDescriptionText(
    modifier: Modifier = Modifier,
    userName: String,
    contentDescription: String? = null,
    timestamp: Long,
    onUserClick: () -> Unit
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    var isTextEllipsized by remember { mutableStateOf(false) }
    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }

    val userNameTag = "USER_NAME"
    val showMoreTag = "SHOW_MORE"
    val showLessTag = "SHOW_LESS"

    val showMoreText = stringResource(R.string.showMore) //больше
    val showLessText = stringResource(R.string.showLess) //меньше

    val annotatedString = buildAnnotatedString {
        pushStringAnnotation(tag = userNameTag, annotation = userName)
        withStyle(style = SpanStyle(fontWeight = FontWeight.Medium)) {
            append(userName)
            append(" ")
        }

        if (!isExpanded && contentDescription != null) {
            val visibleText = contentDescription.take(minOf(20, contentDescription.length))
            append(visibleText)
            append(" ")
            append("...")

            pushStringAnnotation(tag = showMoreTag, annotation = showMoreTag)
            withStyle(SpanStyle(color = Color.Gray, fontWeight = FontWeight.Medium)) {
                append(showMoreText)
            }
            pop()
        } else {
            append(contentDescription ?: "")
            append(". ")

            pushStringAnnotation(tag = showLessTag, annotation = showLessTag)
            withStyle(style = SpanStyle(color = Color.Gray, fontWeight = FontWeight.Medium)) {
                append(showLessText)
            }
            pop()
        }
    }

    Column(
        modifier = modifier
    ) {
        Text(
            text = annotatedString,
            maxLines = if (isExpanded) Int.MAX_VALUE else 1,
            onTextLayout = { result ->
                textLayoutResult = result
                isTextEllipsized = result.isLineEllipsized(0)
                Log.d("PostDescriptionText", "TextLayout updated, hasEllipsis: $isTextEllipsized")
            },
            modifier = Modifier
                .pointerInput(annotatedString) {
                    detectTapGestures { offset ->
                        textLayoutResult?.let { layout ->
                            val pos = layout.getOffsetForPosition(offset)

                            val userName = annotatedString.getStringAnnotations(userNameTag, pos, pos)
                            if (userName.isNotEmpty()) {
                                onUserClick()
                            }

                            val more = annotatedString.getStringAnnotations(showMoreTag, pos, pos)
                            if (more.isNotEmpty()) {
                                isExpanded = true
                            }

                            val less = annotatedString.getStringAnnotations(showLessTag, pos, pos)
                            if (less.isNotEmpty()) {
                                isExpanded = false
                            }

                            val charAtPos =
                                if (pos < annotatedString.length) annotatedString.text[pos] else "out of bounds"
                            Log.d("PostDescriptionText", "Character at pos $pos: $charAtPos")
                        }
                    }
                }
                .animateContentSize(animationSpec = tween(durationMillis = 300))
        )

        Text(
            text = fromTimeStampToTimeAgo(timestamp),
            color = Color.Gray,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun fromTimeStampToTimeAgo(timeStamp: Long): String {
    val dataTime = Instant.ofEpochMilli(timeStamp)
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()

    return formatTimeAgo(dataTime)
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatTimeAgo(dateTime: LocalDateTime): String {
    val now = LocalDateTime.now()
    val duration = Duration.between(dateTime, now)

    val seconds = duration.seconds
    val minutes = duration.toMinutes()
    val hours = duration.toHours()
    val days = duration.toDays()

    return when {
        seconds < 60 -> "just now"
        minutes < 60 -> "$minutes minutes ago"
        hours < 24 -> "$hours hours ago"
        days == 1L -> "yesterday"
        days < 7 -> "$days days ago"
        days < 365 -> "$days days ago"
        else -> {
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            dateTime.format(formatter)
        }
    }
}