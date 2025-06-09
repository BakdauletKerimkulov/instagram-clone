package com.example.presentation.ui_auth

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.presentation.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecoverPasswordScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state by viewModel.authState.collectAsState()

    AuthModule(
        modifier = modifier,
        onBackClick = onBackClick,
        nextButton = {
            Button(onClick = onContinueClick) {
                Text(text = "Continue")
            }
        },
        headText = "Find your account",
        text = "Enter your email or username.\nCan't reset your password?",
        supportText = "You may receive WhatsApp and SMS notifications from us for security and login purposes",
        textField = {
            InputField(
                value = state.loginInput,
                onValueChange = { viewModel.updateLoginInput(it) },
                label = "Password",
                isError = state.loginInputError != null,
                supportingText = {
                    state.loginInputError?.let {
                        Text(text = it.message)
                    }
                }
            ) {
                if (state.loginInput.isNotBlank()) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.close),
                        modifier = Modifier.clickable { viewModel.updateLoginInput("") }
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewRecoverPasswordScreen() {
    RecoverPasswordScreen(
        onBackClick = {},
        onContinueClick = {}
    )
}