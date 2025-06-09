package com.example.presentation.ui_auth

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.presentation.R
import com.example.presentation.common.utils.showToast
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FillNumberScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onAuthEmailClick: () -> Unit,
    onAutoVerified: () -> Unit,
    onCodeSent: (String) -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val state by viewModel.authState.collectAsState()
    val context = LocalContext.current
    val activity = context.findActivity()

    LaunchedEffect(Unit) {
        viewModel.setAuthMode(AuthMode.Register)
    }

    LaunchedEffect(Unit) {
        viewModel.authEvents.collectLatest { event ->
            when (event) {
                is AuthEvent.Message -> {
                    showToast(context, event.message)
                }
                is AuthEvent.Success -> {
                    onAutoVerified()
                }
            }
        }
    }

    AuthModule(
        modifier = modifier,
        onBackClick = onBackClick,
        nextButton = {
            Button(
                onClick = {
//                    if (BuildConfig.DEBUG) {
//                        if (viewModel.validateLoginInput()) onCodeSent(state.loginInput)
//                    } else {
                        activity?.let {
                            viewModel.sendVerificationCode(it, state.loginInput) { onCodeSent(state.loginInput) }
                        } ?: run {
                            Toast.makeText(context, "Activity not found", Toast.LENGTH_SHORT).show()
                        }
                    //}
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.background,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(text = stringResource(R.string.nextBtn))
                }
            }
        },
        headText = "What's your mobile number?",
        text = "Enter the mobile phoneNumber where you can be contacted. " +
                "No one will see this on your profile.",
        supportText = "You may receive WhatsApp and SMS notifications from us for security and login purposes.",
        textField = {
            InputField(
                value = state.loginInput,
                onValueChange = { viewModel.updateLoginInput(it) },
                label = "Mobile number",
                isError = state.loginInputError != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                trailingIcon = {
                    if (state.loginInput.isNotBlank()) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.close),
                            modifier = Modifier.clickable(onClick = { viewModel.updateLoginInput("") })
                        )
                    }
                },
                supportingText = {
                    state.loginInputError?.let {
                        Text(text = it.message)
                    }
                }
            )
        }
    ) {
        OutlinedButton(onClick = onAuthEmailClick, modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(R.string.signupWithEmail))
        }
    }
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewAuthWithNumberScreen() {
//    FillNumberScreen(
//        onBackClick = {},
//        onAuthEmailClick = {},
//        onAutoVerified = {},
//        onCodeSent = {}
//    )
//}

@Preview(showBackground = true)
@Composable
fun A (
    modifier: Modifier = Modifier
) {
    var count by remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            while (true) {
                count++
                delay(200)
                if (count == 100) {
                    count = 0
                }
            }
        }

        scope.launch {

        }
    }

    Box(modifier = modifier.size(200.dp), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            progress = {
                count.toFloat()
            },
        )
    }
}