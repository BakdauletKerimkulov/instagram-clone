package com.example.presentation.ui_auth

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull

@Composable
fun VerificationScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onNextClick: (AuthMode) -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state by viewModel.authState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.authEvents.collectLatest { event ->
            when (event) {
                is AuthEvent.Success -> {
                    onNextClick(state.authMode)
                }

                is AuthEvent.Message -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    AuthModule(
        modifier = modifier,
        onBackClick = onBackClick,
        nextButton = {
            Button(
                onClick = { viewModel.verifyCode(state.verificationCode) },
                modifier = Modifier.fillMaxWidth()
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(text = "Next")
                }
            }
        },
        headText = "Enter the confirmation verificationCode",
        text = "To confirm your account, enter the 6-digit verificationCode sent to ${state.loginInput}",
        textField = {
            OtpCodeInput(
                code = state.verificationCode,
                onCodeChange = { viewModel.updateVerificationCode(it.trim()) },
                isError = state.verificationCodeError != null,
                errorMessage = state.verificationCodeError?.message
            )
        }
    ) {
        OutlinedButton(
            onClick = { },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "I didn't get the verificationCode")
        }
    }
}

@Composable
fun OtpCodeInput(
    modifier: Modifier = Modifier,
    code: String,
    onCodeChange: (String) -> Unit,
    length: Int = 6,
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true,
) {
    val focusRequesters = remember { List(length) { FocusRequester() } }
    val keyboardController = LocalSoftwareKeyboardController.current
    val clipboardManager = LocalClipboard.current

    // Автофокус на первое поле
    LaunchedEffect(Unit) {
        focusRequesters[0].requestFocus()
    }

    // Обработка буфера обмена
    LaunchedEffect(Unit) {
        snapshotFlow { clipboardManager.toString() }
            .filterNotNull()
            .collect { clipboardText ->
                val cleanedCode = clipboardText.filter { it.isDigit() }.take(length)
                if (cleanedCode.length == length) {
                    onCodeChange(cleanedCode)
                    focusRequesters[length - 1].requestFocus()
                    keyboardController?.hide()
                }
            }
    }
    Column {

        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        ) {
            repeat(length) { index ->
                val char = code.getOrNull(index)?.toString() ?: ""
                val isFocused = code.length == index && enabled

                val borderColor by animateColorAsState(
                    targetValue = when {
                        isError -> MaterialTheme.colorScheme.error
                        isFocused -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.outline
                    },
                    animationSpec = tween(200)
                )

                BasicTextField(
                    value = char,
                    onValueChange = { newChar ->
                        if (newChar.length <= 1 && newChar.all { it.isDigit() }) {
                            val newCode = buildString {
                                append(code.take(index))
                                append(newChar)
                                append(code.drop(index + 1))
                            }.take(length)
                            onCodeChange(newCode)

                            // Переход фокуса вперёд при вводе
                            if (newChar.isNotEmpty() && index < length - 1) {
                                focusRequesters[index + 1].requestFocus()
                            }
                            // Переход фокуса назад при удалении
                            else if (newChar.isEmpty() && index > 0) {
                                focusRequesters[index - 1].requestFocus()
                            }
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .focusRequester(focusRequesters[index])
                        .clip(RoundedCornerShape(8.dp))
                        .border(2.dp, borderColor, RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surface),
                    enabled = enabled,
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = if (index == length - 1) ImeAction.Done else ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            if (index < length - 1) focusRequesters[index + 1].requestFocus()
                        },
                        onDone = { keyboardController?.hide() }
                    ),
                    decorationBox = { innerTextField ->
                        Box(contentAlignment = Alignment.Center) {
                            if (char.isEmpty() && !isFocused) {
                                Text(
                                    text = "−",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        textAlign = TextAlign.Center,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                            alpha = 0.4f
                                        )
                                    )
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }
        }

        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAuthCheckCodeScreen() {
    VerificationScreen(
        onBackClick = {},
        onNextClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewOtpCodeInput() {
    OtpCodeInput(
        code = "111111",
        onCodeChange = { }
    )
}