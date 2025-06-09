package com.example.presentation.ui_auth

import android.app.Activity
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.domain.CreateUserUseCase
import com.example.domain.SignInEmailUseCase
import com.example.domain.SignOutUseCase
import com.example.domain.SignUpEmailUseCase
import com.example.domain.UpdateDisplayNameUseCase
import com.example.domain.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val signInEmailUseCase: SignInEmailUseCase,
    private val signUpEmailUseCase: SignUpEmailUseCase,
    private val phoneAuthManager: PhoneAuthManager,
    private val createUserUseCase: CreateUserUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val updateDisplayNameUseCase: UpdateDisplayNameUseCase,
) : ViewModel() {
    private val _authState = MutableStateFlow(AuthState())
    val authState = _authState.asStateFlow()

    private val _authEvents = MutableSharedFlow<AuthEvent>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val authEvents = _authEvents.asSharedFlow()

    private var pendingUser: PendingUser? = null

    init {
        Log.d("AuthViewModel", "AuthViewModel created")
    }

    override fun onCleared() {
        Log.d("AuthViewModel", "AuthViewModel cleared!")
    }

    fun updateLoginInput(input: String) {
        _authState.update {
            it.copy(loginInput = input, loginInputError = null)
        }
    }

    fun updateVerificationCode(code: String) {
        _authState.update { it.copy(verificationCode = code.trim(), verificationCodeError = null) }
    }

    fun updatePassword(password: String) {
        _authState.update { it.copy(password = password, passwordError = null) }
    }

    fun updateBirthday(birthday: Long?) {
        _authState.update { it.copy(birthday = birthday) }
    }

    fun togglePasswordVisibility() {
        _authState.update { it.copy(passwordVisible = !it.passwordVisible) }
    }

    fun setAuthMode(mode: AuthMode) {
        _authState.update {
            it.copy(authMode = mode)
        }
    }

    fun updateUsername(username: String) {
        _authState.update { it.copy(username = username, usernameError = null) }
    }

    fun validateLoginInput(): Boolean {
        var newLoginState = authState.value.validateLoginInput()

        if (newLoginState.authMode == AuthMode.Register) {
            newLoginState = newLoginState.validateUsername()
        }
        _authState.value = newLoginState
        return newLoginState.loginInputError == null
    }

    fun validatePassword(): Boolean {
        val newState = authState.value.validatePassword()
        _authState.value = newState
        return newState.passwordError == null
    }

    fun validateCode(): Boolean {
        val newState = authState.value.validateVerificationCode()
        _authState.value = newState
        return newState.verificationCodeError == null
    }

    fun validateUsername(): Boolean {
        val newState = authState.value.validateUsername()
        _authState.value = newState
        return newState.usernameError == null
    }

    fun signInMethod(activity: Activity) {
        var state = authState.value

        if (state.loginInput.isBlank()) {
            _authState.update { it.copy(loginInputError = FormError.Required) }
            return
        }

        state = state.validateLoginInput()

        _authState.value = state

        when (authState.value.inputType) {
            InputType.Email -> signInWithEmailAndPassword(state.loginInput, state.password)
            InputType.Phone -> sendVerificationCode(
                activity,
                state.loginInput
            ) { _authEvents.tryEmit(AuthEvent.Success) }

            InputType.Username -> {
                _authEvents.tryEmit(AuthEvent.Message("Username input type is not supported yet"))
                _authState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun signInWithEmailAndPassword(email: String, password: String) {
        if (!validatePassword()) return

        _authState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            signInEmailUseCase(email, password).fold(
                onSuccess = {
                    _authState.update { it.copy(isLoading = false) }
                    _authEvents.tryEmit(AuthEvent.Success)
                },
                onFailure = { e ->
                    _authState.update {
                        it.copy(
                            isLoading = false,
                            loginInputError = FormError.Custom(e.message ?: "Unknown error")
                        )
                    }
                }
            )
        }
    }

    fun sendVerificationCode(activity: Activity, loginInput: String, onCodeSent: () -> Unit = { }) {

        _authState.update { it.copy(isLoading = true) }

        phoneAuthManager.startPhoneNumberVerification(
            activity,
            loginInput,
            onVerificationCompleted = { credential ->
                signInWithPhoneAuthCredential(credential)
            },
            onVerificationFailed = { e ->
                _authState.update {
                    it.copy(
                        isLoading = false,
                        verificationCodeError = handleError(e)
                    )
                }
            },
            onCodeSent = { verificationId, _ ->
                _authState.update {
                    Log.d("AuthViewModel", "onCodeSent: $verificationId")
                    it.copy(isLoading = false, verificationId = verificationId)
                }
                onCodeSent()
            }
        )
    }

    fun verifyCode(code: String) {
        if (!validateCode()) return

        val credential = phoneAuthManager.getCredential(code)

        if (credential == null) {
            _authState.update {
                it.copy(
                    isLoading = false,
                    verificationCodeError = FormError.Custom("Missing verification ID")
                )
            }
            return
        }

        _authState.update { it.copy(isLoading = true) }
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(
        credential: PhoneAuthCredential,
    ) {
        viewModelScope.launch {
            auth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    _authState.update { it.copy(isLoading = false) }

                    if (task.isSuccessful) {
                        _authEvents.tryEmit(AuthEvent.Success)
                        val user = task.result?.user
                        if (user != null) {
                            pendingUser = PendingUser(
                                uid = user.uid, phoneNumber = user.phoneNumber
                            )
                        }
                    } else {
                        _authState.update {
                            it.copy(
                                verificationCodeError = handleError(task.exception ?: Exception())
                            )
                        }

                        _authEvents.tryEmit(
                            AuthEvent.Message(
                                task.exception?.message ?: "Unknown error"
                            )
                        )
                    }
                }
        }
    }

    fun signUpWithEmailAndPassword(email: String, password: String) {
        _authState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            signUpEmailUseCase(email, password).fold(
                onSuccess = { user ->
                    _authState.update { it.copy(isLoading = false) }

                    Log.d("AuthViewModel", "signUpWithEmailAndPassword: $user")
                    if (user != null) pendingUser = PendingUser(uid = user.uid, email = user.email)

                    _authEvents.tryEmit(AuthEvent.Success)
                },
                onFailure = { e ->
                    if (e is TimeoutCancellationException) {
                        _authState.update { it.copy(isLoading = false) }
                        _authEvents.tryEmit(AuthEvent.Message("Timeout exceeded"))
                    } else {
                        _authState.update { it.copy(isLoading = false) }
                        _authEvents.tryEmit(AuthEvent.Message(e.message ?: "Unknown error"))
                    }
                }
            )
        }
    }

    fun completeUserProfile() {
        if (!validateUsername()) return

        Log.d("AuthViewModel", "completeUserProfile: $pendingUser")

        val user = pendingUser
        val state = authState.value

        if (user == null) {
            _authEvents.tryEmit(AuthEvent.Message("User not registered yet"))
            return
        }

        val fullUser = User(
            uid = user.uid,
            email = user.email,
            phoneNumber = user.phoneNumber,
            username = state.username,
            birthday = state.birthday
        )

        _authState.update { it.copy(isLoading = true) }

        viewModelScope.launch {

            val result = updateDisplayNameUseCase(state.username).fold(
                onSuccess = {
                    createUserUseCase(fullUser)
                },
                onFailure = {
                    Result.failure(it)
                }
            )

            result.fold(
                onSuccess = {
                    _authState.update { it.copy(isLoading = false) }
                    _authEvents.tryEmit(AuthEvent.Success)
                },
                onFailure = {
                    _authState.update { it.copy(isLoading = false) }
                    signOutUseCase()
                    _authEvents.tryEmit(AuthEvent.Message("Cannot create user: ${it.message}"))
                }
            )
        }
    }

    private fun handleError(e: Exception): FormError = when (e) {
        is FirebaseAuthInvalidCredentialsException -> when (e.errorCode) {
            "ERROR_INVALID_PHONE_NUMBER" -> FormError.InvalidFormat
            "ERROR_MISSING_PHONE_NUMBER" -> FormError.Custom("Phone number is missing")
            else -> FormError.Custom(e.message ?: "Invalid credentials error")
        }

        is FirebaseAuthException -> when (e.errorCode) {
            "ERROR_TOO_MANY_REQUESTS" -> FormError.Custom("Too many requests")
            "ERROR_INVALID_VERIFICATION_CODE" -> FormError.Custom("Invalid verification code")
            else -> FormError.Custom(e.message ?: "Auth error: ${e.errorCode}")
        }

        else -> FormError.Custom(e.message ?: "Unknown error")
    }
}

data class PendingUser(
    val uid: String,
    val email: String? = null,
    val phoneNumber: String? = null,
    val username: String? = null,
    val birthday: Long? = null,
)

enum class AuthMode {
    Login, Register
}

enum class InputType {
    Email, Phone, Username
}

data class AuthState(
    val isLoading: Boolean = false,

    val authMode: AuthMode = AuthMode.Login,

    val loginInput: String = "",
    val loginInputError: FormError? = null,
    val inputType: InputType = InputType.Username,

    val verificationCode: String = "",
    val verificationCodeError: FormError? = null,

    //Юзернейм только для регистрации
    val username: String = "",
    val usernameError: FormError? = null,

    val verificationId: String? = null,

    //День рождения
    val birthday: Long? = null,

    //Пароль
    val password: String = "",
    val passwordError: FormError? = null,
    val passwordVisible: Boolean = false,
) {
    fun validateLoginInput(): AuthState {
        if (loginInput.isBlank()) return copy(loginInputError = FormError.Required)

        val detectedType = detectInputType(loginInput)

        val error = when (detectedType) {
            InputType.Phone -> {
                if (!loginInput.startsWith("+") || !loginInput.matches(Regex("\\+\\d{10,14}"))) FormError.InvalidFormat
                else null
            }

            InputType.Email -> {
                if (!Patterns.EMAIL_ADDRESS.matcher(loginInput).matches()) FormError.InvalidFormat
                else null
            }

            InputType.Username -> null
        }

        return copy(
            loginInputError = error,
            inputType = detectedType
        )
    }

    fun validatePassword(): AuthState {
        val error = when {
            password.isBlank() -> FormError.Required
            password.length < 6 -> FormError.PasswordTooShort
            else -> null
        }
        return copy(passwordError = error)
    }

    fun validateVerificationCode(): AuthState {
        val error =
            if (verificationCode.isBlank()) FormError.Required
            else if (verificationCode.length != 6) FormError.InvalidFormat
            else null
        return copy(verificationCodeError = error)
    }

    fun validateUsername(): AuthState {
        val error = if (username.isBlank()) FormError.Required else null
        return copy(usernameError = error)
    }

    companion object {
        fun detectInputType(input: String): InputType = when {
            Patterns.EMAIL_ADDRESS.matcher(input).matches() -> InputType.Email
            Patterns.PHONE.matcher(input).matches() -> InputType.Phone
            else -> InputType.Username
        }
    }
}

sealed class AuthEvent {
    data class Message(val message: String) : AuthEvent()
    object Success : AuthEvent()
}

sealed class FormError(val message: String) {
    object Required : FormError("Field is required")
    object InvalidFormat : FormError("Incorrect format")

    object PasswordTooShort : FormError("Password too short")
    data class Custom(val text: String) : FormError(text)
}