package com.example.parcialtp3.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parcialtp3.shared.DemoDialogHost
import com.example.parcialtp3.shared.rememberDemoDialog
import com.example.parcialtp3.shared.VoraCard
import com.example.parcialtp3.shared.VoraChip
import com.example.parcialtp3.shared.VoraIconButton
import com.example.parcialtp3.shared.VoraLimeButton
import com.example.parcialtp3.shared.VoraOrb
import com.example.parcialtp3.shared.VoraScreen
import com.example.parcialtp3.shared.VoraSoftBackground
import com.example.parcialtp3.shared.VoraTextField
import com.example.parcialtp3.shared.VoraTextLink
import com.example.parcialtp3.ui.theme.VoraGreen
import com.example.parcialtp3.ui.theme.VoraTextMuted

/*
 * Login Flow (solo visual). Los campos aceptan texto pero no validan ni autentican:
 * el login real (Firebase) es de otra etapa.
 */

@Composable
private fun LoginHeader(subtitle: String?) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Box {
            VoraOrb(size = 64.dp)
            Text(
                "AI",
                color = androidx.compose.ui.graphics.Color.White,
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

/** Login - 01 SignIn. Primera pantalla de la app: no tiene "atras". */
@Composable
fun SignInScreen(
    onSignInClick: () -> Unit,
    onSignUpClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val demo = rememberDemoDialog()
    VoraSoftBackground(modifier) {
        DemoDialogHost(demo)
        VoraScreen(background = androidx.compose.ui.graphics.Color.Transparent) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                VoraChip("EN ⌄", onClick = { demo.show("El idioma es solo inglés en esta versión de demostración.") })
            }
            LoginHeader("Your strength, nutrition, and progress plan in one place.")
            VoraCard {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    VoraTextField("Email Address", "alex.morgan@email.com", leading = "✉", filled = true)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Password", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                        VoraTextLink("Forgot your password?", onClick = { demo.show("Recuperar la contraseña no está disponible: el acceso es una demostración sin Firebase.") })
                    }
                    VoraTextField("", "••••••••••", leading = "🔑", trailing = "👁", filled = true)
                    VoraLimeButton("Sign In  →", onSignInClick)
                    Text(
                        "Or continue with",
                        style = MaterialTheme.typography.bodySmall,
                        color = VoraTextMuted,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    SocialRow(onSignInClick)
                }
            }
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
}

/** Login - 02 SignUp. */
@Composable
fun SignUpScreen(
    onSignUpClick: () -> Unit,
    onSignInClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val demo = rememberDemoDialog()
    VoraSoftBackground(modifier) {
        DemoDialogHost(demo)
        VoraScreen(background = androidx.compose.ui.graphics.Color.Transparent) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                VoraIconButton("←", onBackClick, size = 40.dp)
                Spacer(Modifier.weight(1f))
                VoraChip("EN ⌄", onClick = { demo.show("El idioma es solo inglés en esta versión de demostración.") })
            }
            LoginHeader(null)
            VoraCard {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    VoraTextField("Full Name", "Alex Morgan", leading = "👤")
                    VoraTextField("Email Address", "alex.morgan@email.com", leading = "✉")
                    VoraTextField("Password", "At least 8 characters", leading = "🔒", trailing = "👁")
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("✔ 8+ secure characters", style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
                        VoraTextLink("Password help?", onClick = { demo.show("La contraseña debe tener al menos 8 caracteres. No se valida en la demostración.") })
                    }
                    VoraLimeButton("Sign Up  →", onSignUpClick)
                    Text(
                        "Or sign up with",
                        style = MaterialTheme.typography.bodySmall,
                        color = VoraTextMuted,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    SocialRow(onSignUpClick)
                }
            }
            Text(
                "🛡 Biometric sign-in",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                Text("Having account issues? ", style = MaterialTheme.typography.bodyMedium)
                VoraTextLink("Recover", onClick = { demo.show("La recuperación de cuenta no está disponible en la demostración.") })
            }
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                Text("Already have an account? ", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                VoraTextLink("Sign In", onSignInClick)
            }
        }
    }
}
