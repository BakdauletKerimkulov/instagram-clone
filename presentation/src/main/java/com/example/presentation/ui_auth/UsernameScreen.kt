package com.example.presentation.ui_auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.presentation.common.utils.showToast
import kotlinx.coroutines.flow.collectLatest

@Composable
fun UsernameScreen(
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
                AuthEvent.Success -> {
                    showToast(context, "Welcome to Instagram!")
                    onNextClick()
                }
                is AuthEvent.Message -> {
                    showToast(context, event.message)
                }
            }
        }
    }

    AuthModule(
        modifier = modifier,
        onBackClick = onBackClick,
        nextButton = {
            Button(
                onClick = { viewModel.completeUserProfile() },
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
        headText = "Create a username",
        text = "Create a username that you'll remember but is difficult for others to guess.",
        textField = {
            InputField(
                value = state.username,
                onValueChange = { viewModel.updateUsername(it) },
                label = "Username",
                isError = state.usernameError != null,
                supportingText = {
                    state.usernameError?.let {
                        Text(text = it.message)
                    }
                }
            ) {
                if (state.username.isNotBlank()) {
                    Icon(
                        imageVector = Icons.Default.Clear, contentDescription = null,
                        modifier = Modifier.clickable { viewModel.updateUsername("") }
                    )
                }
            }
        }
    )
}