package com.example.presentation.ui_auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.presentation.R

@Composable
fun FillEmailScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onAuthMobileClick: () -> Unit,
    onNextClick: (String) -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val state by viewModel.authState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.setAuthMode(AuthMode.Register)
    }

    AuthModule(
        modifier = modifier,
        onBackClick = onBackClick,
        nextButton = {
            Button(
                onClick = {
                    if (viewModel.validateLoginInput()) {
                        onNextClick(state.loginInput)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator()
                } else {
                    Text(text = stringResource(R.string.nextBtn))
                }
            }
        },
        headText = "What's your email?",
        text = "Enter the email where you can be contacted. No one will see this this on your phone",
        textField = {
            InputField(
                value = state.loginInput,
                onValueChange = { viewModel.updateLoginInput(it) },
                label = "Email",
                isError = state.loginInputError != null,
                supportingText = {
                    state.loginInputError?.let {
                        Text(text = it.message)
                    }
                },
                trailingIcon = {
                    if (state.loginInput.isNotBlank()) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.close),
                            modifier = Modifier.clickable(onClick = { viewModel.updateLoginInput("") })
                        )
                    }
                }
            )
        }
    ) {
        OutlinedButton(onClick = onAuthMobileClick, modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(R.string.signupWithNumber))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAuthWithEmailScreen() {
    FillEmailScreen(
        onBackClick = {},
        onAuthMobileClick = {},
        onNextClick = {}
    )
}