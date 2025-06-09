package com.example.presentation.ui_auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.presentation.R
import com.example.presentation.common.utils.showToast
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel(),
    onCreateAccClick: () -> Unit,
    onLoginClick: (String) -> Unit,
    onForgotPasswordClick: () -> Unit,
) {
    val state by viewModel.authState.collectAsState()
    val context = LocalContext.current
    val activity = context.findActivity()

    LaunchedEffect(Unit) {
        viewModel.authEvents.collectLatest { event ->
            when (event) {
                is AuthEvent.Message -> {
                    showToast(context, event.message)
                }

                is AuthEvent.Success -> {
                    onLoginClick(state.inputType.name)
                }
            }
        }
    }

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        val createButton = createRef()
        val mainIcon = createRef()
        val loginColumn = createRef()

        Image(
            painter = painterResource(R.drawable.instagram_main_icon),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .constrainAs(mainIcon) {
                    top.linkTo(parent.top, margin = 100.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        Column(
            modifier = Modifier
                .constrainAs(loginColumn) {
                    top.linkTo(mainIcon.bottom, margin = 80.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                    width = Dimension.fillToConstraints
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.loginInput,
                onValueChange = { viewModel.updateLoginInput(it) },
                singleLine = true,
                label = { Text(text = "Username, email or mobile number") },
                isError = state.loginInputError != null,
                supportingText = {
                    state.loginInputError?.let {
                        Text(it.message)
                    }
                },
                trailingIcon = {
                    if (state.loginInput.isNotBlank()) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.close),
                            modifier = Modifier.clickable { viewModel.updateLoginInput("") }
                        )
                    }
                },
                shape = RoundedCornerShape(16.dp)
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                value = state.password,
                onValueChange = { viewModel.updatePassword(it) },
                singleLine = true,
                label = { Text(text = "Password") },
                isError = state.passwordError != null,
                supportingText = {
                    state.passwordError?.let {
                        Text(text = it.message)
                    }
                },
                trailingIcon = {
                    Icon(
                        painter = painterResource(if (state.passwordVisible) R.drawable.visibility_off_24px else R.drawable.visibility_24px),
                        contentDescription = "Visibility",
                        modifier = Modifier.clickable {
                            viewModel.togglePasswordVisibility()
                        }
                    )
                },
                shape = RoundedCornerShape(16.dp),
                visualTransformation = if (state.passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
            )

            Button(
                onClick = {
                    activity?.let { viewModel.signInMethod(activity = it) }
                        ?: showToast(context, "Activity is null")
                },
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(text = stringResource(R.string.loginButton))
                }
            }

            TextButton(
                onClick = onForgotPasswordClick
            ) {
                Text("Forgot password?")
            }
        }

        OutlinedButton(
            onClick = onCreateAccClick,
            modifier = Modifier
                .constrainAs(createButton) {
                    bottom.linkTo(parent.bottom, margin = 32.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                    width = Dimension.fillToConstraints
                },
            border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary)
        ) {
            Text(text = stringResource(R.string.createAcc))
        }

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAuthMainScreen() {
    LoginScreen(
        onCreateAccClick = {},
        onLoginClick = {},
        onForgotPasswordClick = {}
    )
}