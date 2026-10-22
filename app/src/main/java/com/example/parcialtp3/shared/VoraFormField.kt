package com.example.parcialtp3.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parcialtp3.ui.theme.VoraError
import com.example.parcialtp3.ui.theme.VoraGreen
import com.example.parcialtp3.ui.theme.VoraTextMuted

/**
 * Campo de formulario CONTROLADO: el texto y el error viven en el ViewModel (value / error) y el
 * campo solo los dibuja. VoraTextField guarda su propio texto y no sirve para validar.
 * Mismo aspecto que VoraTextField (etiqueta arriba, campo de radio 12), mas el mensaje de error.
 */
@Composable
fun VoraFormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    error: String? = null,
    decimal: Boolean = false,
    labelUppercase: Boolean = true,
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        if (labelUppercase) VoraOverline(label) else Text(label, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            isError = error != null,
            placeholder = { Text(placeholder, color = VoraTextMuted, fontSize = 14.sp) },
            supportingText = error?.let { { Text(it) } },
            keyboardOptions = KeyboardOptions(keyboardType = if (decimal) KeyboardType.Decimal else KeyboardType.Text),
            textStyle = TextStyle(fontSize = 15.sp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = VoraGreen,
                unfocusedBorderColor = VoraTextMuted,
                errorBorderColor = VoraError,
            ),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
