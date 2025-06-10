package com.example.presentation.ui_create

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.presentation.common.utils.showToast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


//Экран для создания фото и видео
@Composable
fun CreateScreen(
    modifier: Modifier = Modifier,
    onPermissionGranted: () -> Unit = {}
) {
    val context = LocalContext.current



    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
        } else {
            showToast(context, "Permission denied")
        }
    }

    SideEffect {
        launcher.launch(Manifest.permission.CAMERA)
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Screen is coming soon")
        }
    }
}

@HiltViewModel
class CreateViewModel @Inject constructor(
) : ViewModel() {

    private val _isPermissionGranted = MutableStateFlow(false)
    val isPermissionGranted = _isPermissionGranted.asStateFlow()

    fun onPermissionResult(isGranted: Boolean) {
        viewModelScope.launch {
            _isPermissionGranted.emit(isGranted)
        }
    }
}