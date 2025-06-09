package com.example.presentation.common.utils

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.presentation.R

@Composable
fun InstagramLogo(
    modifier: Modifier = Modifier,
    fontSize: Int = 36
) {
    Text(
        text = stringResource(R.string.instagramLogo),
        fontFamily = FontFamily(Font(R.font.billabong, FontWeight.Bold)),
        fontSize = fontSize.sp,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = modifier
    )
}