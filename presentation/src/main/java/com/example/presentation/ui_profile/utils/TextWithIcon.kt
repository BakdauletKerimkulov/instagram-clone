package com.example.presentation.ui_profile.utils

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.presentation.R

@Composable
fun TextWithIcon(
    modifier: Modifier = Modifier,
    email: String,
    icon: Int
) {
    val iconId = "icon"
    val text = buildAnnotatedString {
        append(email)
        append(" ")
        appendInlineContent(
            id = iconId,
            "[icon]"
        )
    }

    val inlineContent = mapOf(
        iconId to InlineTextContent(
            Placeholder(
                width = 24.sp,
                height = 24.sp,
                placeholderVerticalAlign = PlaceholderVerticalAlign.Center
            )
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null
            )
        }
    )

    Text(
        text = text,
        inlineContent = inlineContent,
        fontSize = 24.sp,
        modifier = modifier,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodyMedium
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewTextWithIcon() {
    TextWithIcon(
        email = "kerimkulov_b",
        icon = R.drawable.carousel_icon,
    )
}