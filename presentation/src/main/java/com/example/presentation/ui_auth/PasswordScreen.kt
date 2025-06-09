package com.example.presentation.ui_auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.presentation.R
import com.example.presentation.common.utils.showToast
import kotlinx.coroutines.flow.collectLatest

@Composable
fun PasswordScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val state by viewModel.authState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.authEvents.collectLatest { event ->
            when (event) {
                is AuthEvent.Success -> onNextClick()
                is AuthEvent.Message -> showToast(context, event.message)
            }
        }
    }

    AuthModule(
        modifier = modifier,
        onBackClick = onBackClick,
        nextButton = {
            Button(
                onClick = { if (viewModel.validatePassword()) viewModel.signUpWithEmailAndPassword(state.loginInput, state.password) },
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
        headText = "Create a password",
        text = "Create a password that you'll remember but is difficult for others to guess.",
        textField = {
            InputField(
                value = state.password,
                onValueChange = { viewModel.updatePassword(it) },
                label = "Password",
                isError = state.passwordError != null,
                supportingText = {
                    state.passwordError?.let {
                        Text(text = it.message)
                    }
                },
                visualTransformation = if (state.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            ) {
                Icon(
                    painter = painterResource(if (state.passwordVisible) R.drawable.visibility_off_24px else R.drawable.visibility_24px),
                    contentDescription = "Visibility",
                    modifier = Modifier.clickable {
                        viewModel.togglePasswordVisibility()
                    }
                )
            }
        },
        supportText = "Your password must be at least 6 characters"
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewAuthPasswordScreen() {
    PasswordScreen(
        onBackClick = {},
        onNextClick = {}
    )
}