package com.example.parcialtp3.shared

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parcialtp3.ui.theme.VoraBorder
import com.example.parcialtp3.ui.theme.VoraError
import com.example.parcialtp3.ui.theme.VoraGreen
import com.example.parcialtp3.ui.theme.VoraMintTint
import com.example.parcialtp3.ui.theme.VoraTextMuted

/**
 * Campo CONTROLADO con el estilo de las pantallas de Login (icono a la izquierda, fondo menta
 * opcional). El texto y el error vienen del ViewModel; el campo solo los dibuja.
 * Con isPassword = true oculta el texto y el 👁 permite mostrarlo.
 */
@Composable
fun VoraAuthField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    leading: String? = null,
    error: String? = null,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    filled: Boolean = false,
) {
    // Mostrar u ocultar la contraseña es solo visual: no hace falta llevarlo al ViewModel.
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    val leadingIcon: (@Composable () -> Unit)? =
        if (leading != null) {
            { Text(leading, fontSize = 16.sp) }
        } else {
            null
        }

    val trailingIcon: (@Composable () -> Unit)? =
        if (isPassword) {
            {
                Text(
                    text = if (passwordVisible) "🙈" else "👁",
                    fontSize = 16.sp,
                    modifier = Modifier
                        .clickable { passwordVisible = !passwordVisible }
                        .padding(8.dp),
                )
            }
        } else {
            null
        }

    val background = if (filled) VoraMintTint else MaterialTheme.colorScheme.surface

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        if (label.isNotEmpty()) {
            Text(label, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        }
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            isError = error != null,
            placeholder = { Text(placeholder, color = VoraTextMuted, fontSize = 14.sp) },
            supportingText = error?.let { message -> { Text(message) } },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            visualTransformation =
                if (isPassword && !passwordVisible) PasswordVisualTransformation()
                else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(
                keyboardType = if (isPassword) KeyboardType.Password else keyboardType,
            ),
            textStyle = TextStyle(fontSize = 15.sp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = background,
                unfocusedContainerColor = background,
                errorContainerColor = background,
                focusedBorderColor = VoraGreen,
                unfocusedBorderColor = if (filled) VoraBorder else VoraTextMuted,
                errorBorderColor = VoraError,
            ),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}