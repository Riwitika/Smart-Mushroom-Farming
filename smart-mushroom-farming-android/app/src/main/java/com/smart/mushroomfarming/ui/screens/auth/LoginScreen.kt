package com.smart.mushroomfarming.ui.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.smart.mushroomfarming.domain.model.AuthState
import com.smart.mushroomfarming.ui.components.MushroomCard
import com.smart.mushroomfarming.ui.components.MushroomTextField
import com.smart.mushroomfarming.ui.components.PrimaryMushroomButton
import com.smart.mushroomfarming.ui.components.Visibility
import com.smart.mushroomfarming.ui.components.VisibilityOff
import com.smart.mushroomfarming.ui.theme.spacing

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onNavigateToDashboard: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    var passwordVisible by remember { mutableStateOf(false) }
    
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is AuthUiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
                is AuthUiEvent.AuthSuccess -> {
                    onNavigateToDashboard()
                }
            }
        }
    }

    LaunchedEffect(uiState.authResult) {
        val result = uiState.authResult
        if (result is AuthState.Error) {
            snackbarHostState.showSnackbar(result.message)
            viewModel.clearResult()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(MaterialTheme.spacing.large)
                    .imePadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Mini Brand Logo
                Canvas(modifier = Modifier.size(64.dp)) {
                    val width = size.width
                    val height = size.height
                    
                    // stem
                    drawRoundRect(
                        color = Color(0xFFC8E6C9),
                        topLeft = Offset(width * 0.38f, height * 0.5f),
                        size = Size(width * 0.24f, height * 0.45f),
                        cornerRadius = CornerRadius(10f, 10f)
                    )

                    // cap
                    val capPath = Path().apply {
                        moveTo(width * 0.15f, height * 0.55f)
                        cubicTo(
                            width * 0.15f, height * 0.15f,
                            width * 0.85f, height * 0.15f,
                            width * 0.85f, height * 0.55f
                        )
                        lineTo(width * 0.15f, height * 0.55f)
                        close()
                    }
                    drawPath(path = capPath, color = Color(0xFF81C784), style = Fill)
                }

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                Text(
                    text = "Welcome Back",
                    style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))

                Text(
                    text = "Sign in to monitor your smart mushroom farm",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.huge))

                // Input Area Card
                MushroomCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = MaterialTheme.spacing.small)
                    ) {
                        MushroomTextField(
                            value = uiState.email,
                            onValueChange = { viewModel.onEmailChanged(it) },
                            label = "Email Address",
                            placeholder = "Enter your email",
                            leadingIcon = Icons.Default.Email,
                            isError = uiState.emailError != null,
                            errorText = uiState.emailError,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.moveFocus(FocusDirection.Down) }
                            )
                        )

                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                        MushroomTextField(
                            value = uiState.password,
                            onValueChange = { viewModel.onPasswordChanged(it) },
                            label = "Password",
                            placeholder = "Enter your password",
                            leadingIcon = Icons.Default.Lock,
                            isError = uiState.passwordError != null,
                            errorText = uiState.passwordError,
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(imageVector = image, contentDescription = "Toggle password visibility")
                                }
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
                                    viewModel.login()
                                }
                            )
                        )
                        
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

                        // Forgot Password Link aligning right
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Text(
                                text = "Forgot Password?",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary
                                ),
                                modifier = Modifier
                                    .clickable { onNavigateToForgotPassword() }
                                    .padding(vertical = MaterialTheme.spacing.small)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

                PrimaryMushroomButton(
                    text = "Sign In",
                    onClick = {
                        focusManager.clearFocus()
                        viewModel.login()
                    },
                    isLoading = uiState.authResult is AuthState.Loading,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

                // Visual Divider line
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.weight(1f).height(1.dp).background(MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)))
                    Text(
                        text = " Or continue with ",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                        modifier = Modifier.padding(horizontal = MaterialTheme.spacing.small)
                    )
                    Spacer(modifier = Modifier.weight(1f).height(1.dp).background(MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)))
                }

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

                // Styled Social SSO Options
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
                ) {
                    MushroomCard(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { /* Trigger Google Auth Flow */ },
                        elevation = 0f
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().height(24.dp)
                        ) {
                            Canvas(modifier = Modifier.size(18.dp)) {
                                drawArc(
                                    color = Color(0xFFEA4335),
                                    startAngle = 180f,
                                    sweepAngle = 180f,
                                    useCenter = true
                                )
                                drawArc(
                                    color = Color(0xFF4285F4),
                                    startAngle = 0f,
                                    sweepAngle = 180f,
                                    useCenter = true
                                )
                            }
                            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                            Text(
                                text = "Google",
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                    
                    MushroomCard(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { /* Trigger Apple Auth Flow */ },
                        elevation = 0f
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().height(24.dp)
                        ) {
                            Canvas(modifier = Modifier.size(18.dp)) {
                                drawCircle(
                                    color = Color.DarkGray,
                                    radius = size.width * 0.35f,
                                    center = Offset(size.width * 0.5f, size.height * 0.55f)
                                )
                                drawCircle(
                                    color = Color.DarkGray,
                                    radius = size.width * 0.15f,
                                    center = Offset(size.width * 0.5f, size.height * 0.2f)
                                )
                            }
                            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                            Text(
                                text = "Apple",
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Don't have an account? ",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                    Text(
                        text = "Sign Up",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.clickable { onNavigateToRegister() }
                    )
                }
            }
        }
    }
}
