package com.example.presentation.ui_auth

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BirthDateScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val state by viewModel.authState.collectAsState()
    var showDatePicker by remember { mutableStateOf(false) }

    val defaultDate = state.birthday?.let { millisToLocalDate(it) } ?: LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.US)
    val formattedDate = defaultDate.format(formatter)
    val age = Period.between(defaultDate, LocalDate.now()).years

    if (showDatePicker) {
        DatePickerModal(
            onDateSelected = {
                viewModel.updateBirthday(it)
            },
            onDismiss = { showDatePicker = false }
        )
    }

    AuthModule(
        modifier = modifier,
        onBackClick = onBackClick,
        nextButton = {
            Button(
                onClick = onNextClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Next")
            }
        },
        headText = "What's your birthday?",
        text = "Use your own birthday, even if this account is for a business, a pet or something else. " +
                "No one will see this unless you choose to share it. Why do I need to provide my birthday?",
        textField = {
            InputField(
                value = formattedDate,
                onValueChange = { },
                label = "Birthday($age year old)",
                readOnly = true,
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onBackground,
                    disabledBorderColor = MaterialTheme.colorScheme.primary,
                    disabledLabelColor = MaterialTheme.colorScheme.primary,
                ),
                modifier = Modifier.clickable { showDatePicker = true }
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit,
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onDateSelected(datePickerState.selectedDateMillis)
                    onDismiss()
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun millisToLocalDate(millis: Long?): LocalDate? {
    return millis?.let {
        Instant.ofEpochMilli(it)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewBirthDateScreen() {
    BirthDateScreen(
        onBackClick = {},
        onNextClick = {},
    )
}