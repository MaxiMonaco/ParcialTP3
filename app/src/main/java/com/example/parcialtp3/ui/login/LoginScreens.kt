package com.example.parcialtp3.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parcialtp3.auth.AuthState
import com.example.parcialtp3.shared.DemoDialogHost
import com.example.parcialtp3.shared.VoraAuthField
import com.example.parcialtp3.shared.VoraCard
import com.example.parcialtp3.shared.VoraChip
import com.example.parcialtp3.shared.VoraIconButton
import com.example.parcialtp3.shared.VoraLimeButton
import com.example.parcialtp3.shared.VoraOrb
import com.example.parcialtp3.shared.VoraScreen
import com.example.parcialtp3.shared.VoraSoftBackground
import com.example.parcialtp3.shared.VoraTextLink
import com.example.parcialtp3.shared.rememberDemoDialog
import com.example.parcialtp3.ui.theme.VoraGreen
import com.example.parcialtp3.ui.theme.VoraTextMuted
import com.example.parcialtp3.viewmodel.AuthFormState

/*
 * Login Flow. Sign In y Sign Up reciben el formulario y el estado de autenticacion ya armados
 * (los crea AuthViewModel en LoginGraph) y avisan con lambdas lo que hace el usuario.
 * Con Firebase configurado autentican de verdad; sin Firebase funcionan como demostracion.
 */

private const val SOCIAL_NOT_AVAILABLE =
    "El acceso con Apple, Google o Face ID no está incluido. Usá email y contraseña."

@Composable
private fun LoginHeader(subtitle: String?) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Box {
            VoraOrb(size = 64.dp)
            Text(
                "AI",
                color = Color.White,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .clip(CircleShape)
                    .background(VoraGreen)
                    .padding(horizontal = 6.dp, vertical = 2.dp),
            )
        }
        Text("VORA Coach", style = MaterialTheme.typography.headlineSmall, color = VoraGreen, modifier = Modifier.padding(top = 12.dp))
        Text(
            "ADAPTIVE INTELLIGENCE",
            color = VoraGreen,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp,
        )
        if (subtitle != null) {
            Text(
                subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp, start = 24.dp, end = 24.dp),
            )
        }
    }
}

@Composable
private fun SocialRow(onClick: () -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
        VoraChip("Apple", onClick = onClick, modifier = Modifier.weight(1f), center = true)
        VoraChip("Google", onClick = onClick, modifier = Modifier.weight(1f), center = true)
        VoraChip("Face ID", onClick = onClick, modifier = Modifier.weight(1f), center = true)
    }
}

/** Boton principal; mientras Firebase responde se reemplaza por un indicador de carga. */
@Composable
private fun SubmitButton(text: String, isLoading: Boolean, onClick: () -> Unit) {
    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(color = VoraGreen, strokeWidth = 3.dp, modifier = Modifier.size(28.dp))
        }
    } else {
        VoraLimeButton(text, onClick)
    }
}

/** Solo se ve sin Firebase configurado: aclara que el acceso es de demostracion. */
@Composable
private fun DemoModeNotice(isDemo: Boolean) {
    if (isDemo) {
        Text(
            "Modo demostración: sin Firebase configurado, se entra sin completar los datos.",
            style = MaterialTheme.typography.bodySmall,
            color = VoraTextMuted,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

/**
 * Muestra en el Snackbar los errores y la confirmacion del email de reseteo, y despues avisa
 * al ViewModel (onMessageShown) para que no se vuelvan a mostrar.
 */
@Composable
private fun AuthMessagesEffect(
    authState: AuthState,
    snackbarHostState: SnackbarHostState,
    onMessageShown: () -> Unit,
) {
    LaunchedEffect(authState) {
        val message = when (authState) {
            is AuthState.Error -> authState.message
            is AuthState.PasswordResetSent -> "Enviamos instrucciones a ${authState.email}"
            else -> null
        }
        if (message != null) {
            snackbarHostState.showSnackbar(message)
            onMessageShown()
        }
    }
}

/** Login - 01 SignIn. Primera pantalla de la app: no tiene "atras". */
@Composable
fun SignInScreen(
    form: AuthFormState,
    authState: AuthState,
    isDemo: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignInClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onMessageShown: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val demo = rememberDemoDialog()
    val snackbarHostState = remember { SnackbarHostState() }
    AuthMessagesEffect(authState, snackbarHostState, onMessageShown)

    Box(modifier = modifier.fillMaxSize()) {
        VoraSoftBackground {
            DemoDialogHost(demo)
            VoraScreen(background = Color.Transparent) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    VoraChip("EN ⌄", onClick = { demo.show("El idioma es solo inglés en esta versión de demostración.") })
                }
                LoginHeader("Your strength, nutrition, and progress plan in one place.")
                VoraCard {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        VoraAuthField(
                            label = "Email Address",
                            value = form.email,
                            onValueChange = onEmailChange,
                            placeholder = "alex.morgan@email.com",
                            leading = "✉",
                            error = form.errors.email,
                            keyboardType = KeyboardType.Email,
                            filled = true,
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Password", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                            VoraTextLink("Forgot your password?", onForgotPasswordClick)
                        }
                        VoraAuthField(
                            label = "",
                            value = form.password,
                            onValueChange = onPasswordChange,
                            placeholder = "••••••••••",
                            leading = "🔑",
                            error = form.errors.password,
                            isPassword = true,
                            filled = true,
                        )
                        SubmitButton("Sign In  →", isLoading = authState == AuthState.Loading, onClick = onSignInClick)
                        Text(
                            "Or continue with",
                            style = MaterialTheme.typography.bodySmall,
                            color = VoraTextMuted,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                        )
                        SocialRow(onClick = { demo.show(SOCIAL_NOT_AVAILABLE) })
                    }
                }
                DemoModeNotice(isDemo)
                Text(
                    "🛡 End-to-end encrypted biometric sign-in",
                    style = MaterialTheme.typography.bodySmall,
                    color = VoraGreen,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
                Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                    Text("Don't have an account? ", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    VoraTextLink("Sign up here", onSignUpClick)
                }
            }
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
        )
    }
}

/** Login - 02 SignUp. */
@Composable
fun SignUpScreen(
    form: AuthFormState,
    authState: AuthState,
    isDemo: Boolean,
    onFullNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignUpClick: () -> Unit,
    onRecoverClick: () -> Unit,
    onSignInClick: () -> Unit,
    onBackClick: () -> Unit,
    onMessageShown: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val demo = rememberDemoDialog()
    val snackbarHostState = remember { SnackbarHostState() }
    AuthMessagesEffect(authState, snackbarHostState, onMessageShown)

    Box(modifier = modifier.fillMaxSize()) {
        VoraSoftBackground {
            DemoDialogHost(demo)
            VoraScreen(background = Color.Transparent) {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    VoraIconButton("←", onBackClick, size = 40.dp)
                    Spacer(Modifier.weight(1f))
                    VoraChip("EN ⌄", onClick = { demo.show("El idioma es solo inglés en esta versión de demostración.") })
                }
                LoginHeader(null)
                VoraCard {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        VoraAuthField(
                            label = "Full Name",
                            value = form.fullName,
                            onValueChange = onFullNameChange,
                            placeholder = "Alex Morgan",
                            leading = "👤",
                            error = form.errors.fullName,
                        )
                        VoraAuthField(
                            label = "Email Address",
                            value = form.email,
                            onValueChange = onEmailChange,
                            placeholder = "alex.morgan@email.com",
                            leading = "✉",
                            error = form.errors.email,
                            keyboardType = KeyboardType.Email,
                        )
                        VoraAuthField(
                            label = "Password",
                            value = form.password,
                            onValueChange = onPasswordChange,
                            placeholder = "At least 8 characters",
                            leading = "🔒",
                            error = form.errors.password,
                            isPassword = true,
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("✔ 8+ secure characters", style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
                            VoraTextLink("Password help?", onClick = { demo.show("La contraseña tiene que tener al menos 8 caracteres.") })
                        }
                        SubmitButton("Sign Up  →", isLoading = authState == AuthState.Loading, onClick = onSignUpClick)
                        Text(
                            "Or sign up with",
                            style = MaterialTheme.typography.bodySmall,
                            color = VoraTextMuted,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                        )
                        SocialRow(onClick = { demo.show(SOCIAL_NOT_AVAILABLE) })
                    }
                }
                DemoModeNotice(isDemo)
                Text(
                    "🛡 Biometric sign-in",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
                Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                    Text("Having account issues? ", style = MaterialTheme.typography.bodyMedium)
                    VoraTextLink("Recover", onRecoverClick)
                }
                Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                    Text("Already have an account? ", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    VoraTextLink("Sign In", onSignInClick)
                }
            }
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
        )
    }
}