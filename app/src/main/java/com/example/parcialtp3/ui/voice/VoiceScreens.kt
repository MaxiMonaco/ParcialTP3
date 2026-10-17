package com.example.parcialtp3.ui.voice

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parcialtp3.shared.VoraAppBar
import com.example.parcialtp3.shared.VoraBigOrb
import com.example.parcialtp3.shared.VoraCard
import com.example.parcialtp3.shared.VoraChip
import com.example.parcialtp3.shared.VoraDarkCard
import com.example.parcialtp3.shared.VoraLimeButton
import com.example.parcialtp3.shared.VoraMicButton
import com.example.parcialtp3.shared.VoraOrb
import com.example.parcialtp3.shared.VoraOutlinedButton
import com.example.parcialtp3.shared.VoraOverline
import com.example.parcialtp3.shared.VoraPrimaryButton
import com.example.parcialtp3.shared.VoraProgressBar
import com.example.parcialtp3.shared.VoraStatusPill
import com.example.parcialtp3.shared.VoraTextLink
import com.example.parcialtp3.ui.theme.VoraBorder
import com.example.parcialtp3.ui.theme.VoraError
import com.example.parcialtp3.ui.theme.VoraGreen
import com.example.parcialtp3.ui.theme.VoraMint
import com.example.parcialtp3.ui.theme.VoraTextMuted

/*
 * AI Voice Flow (solo visual). No hay microfono ni IA: cada pantalla es un estado del asistente
 * con texto de ejemplo. Los taps sobre el orbe o las tarjetas avanzan al estado siguiente.
 */

@Composable
private fun DarkVoiceFrame(modifier: Modifier, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VoraGreen)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        content = content,
    )
}

/** Voice / 01 — Idle. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VoiceIdleScreen(
    onBackClick: () -> Unit,
    onTapToTalkClick: () -> Unit,
    onTypeInsteadClick: () -> Unit,
    onSuggestedQuestionClick: () -> Unit,
    onSimulateErrorClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        VoraAppBar("VORA Coach", onBackClick, trailing = { VoraStatusPill("ONLINE") })
        Spacer(Modifier.height(32.dp))
        VoraBigOrb(size = 190.dp)
        Spacer(Modifier.height(32.dp))
        Text("How can I help?", style = MaterialTheme.typography.headlineMedium)
        Text(
            "Talk naturally about your workout, nutrition or progress.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp),
        )
        Spacer(Modifier.height(24.dp))
        VoraOverline("Suggested questions")
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(top = 12.dp).fillMaxWidth(),
        ) {
            listOf("Start my workout", "Log my breakfast", "What should I eat tonight?", "How am I doing?").forEach {
                VoraChip(it, onClick = onSuggestedQuestionClick)
            }
        }
        Spacer(Modifier.height(24.dp))
        VoraCard {
            Row {
                VoraOverline("Last conversation", Modifier.weight(1f))
                Text("2 hours ago", style = MaterialTheme.typography.bodySmall, color = VoraTextMuted)
            }
            Text("\"Create a 30-minute workout for me.\"", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 4.dp))
        }
        Spacer(Modifier.height(32.dp))
        VoraMicButton(onTapToTalkClick)
        Text("TAP TO TALK", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = VoraGreen, modifier = Modifier.padding(top = 10.dp))
        VoraTextLink("Type instead", onTypeInsteadClick, modifier = Modifier.padding(top = 8.dp), muted = true)
        // Solo para la demo: en la app real el error aparece cuando falla el microfono.
        VoraTextLink("Simulate error (demo)", onSimulateErrorClick, muted = true)
    }
}

/** Voice / 02 — Listening. */
@Composable
fun VoiceListeningScreen(
    onBackClick: () -> Unit,
    onCancelClick: () -> Unit,
    onFinishedListeningClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DarkVoiceFrame(modifier) {
        VoraAppBar("Listening", onBackClick, dark = true, trailing = {
            Text("● LIVE AUDIO", color = VoraMint, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        })
        Spacer(Modifier.height(36.dp))
        // Tocar el orbe simula que el usuario termino de hablar.
        VoraBigOrb(size = 210.dp, modifier = Modifier.clip(CircleShape).clickable(onClick = onFinishedListeningClick))
        Spacer(Modifier.height(28.dp))
        VoraDarkCard {
            Text("LIVE TRANSCRIPT", color = VoraMint, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            Text(
                "\"I had chicken, rice and some vegetables for lunch...\"",
                color = Color.White,
                fontSize = 20.sp,
                lineHeight = 28.sp,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
        Spacer(Modifier.height(28.dp))
        Text("Speak naturally. I'm listening.", color = Color.White.copy(alpha = 0.7f), style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.weight(1f))
        VoraMicButton(onFinishedListeningClick, mint = true, size = 72.dp)
        Text(
            "Cancel",
            color = VoraMint,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(top = 12.dp).clickable(onClick = onCancelClick).padding(8.dp),
        )
    }
}

/** Voice / 03 — Processing. */
@Composable
fun VoiceProcessingScreen(
    onBackClick: () -> Unit,
    onCancelClick: () -> Unit,
    onFinishedProcessingClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DarkVoiceFrame(modifier) {
        VoraAppBar("Processing", onBackClick, dark = true)
        Spacer(Modifier.height(60.dp))
        // Tocar el orbe simula que VORA termino de pensar.
        VoraBigOrb(size = 180.dp, modifier = Modifier.clip(CircleShape).clickable(onClick = onFinishedProcessingClick))
        Spacer(Modifier.height(28.dp))
        Text("THINKING...", color = VoraMint, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(16.dp))
        VoraDarkCard {
            Text(
                "\"I had chicken, rice and some vegetables for lunch.\"",
                color = Color.White.copy(alpha = 0.9f),
                style = MaterialTheme.typography.bodyLarge,
            )
        }
        Spacer(Modifier.height(32.dp))
        Text("↻  Checking your nutrition target...", color = Color.White.copy(alpha = 0.7f), style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.weight(1f))
        Text(
            "Cancel",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 15.sp,
            modifier = Modifier.clickable(onClick = onCancelClick).padding(12.dp),
        )
    }
}

/** Voice / 04 — Speaking. */
@Composable
fun VoiceSpeakingScreen(
    onBackClick: () -> Unit,
    onTapToInterruptClick: () -> Unit,
    onOpenConversationClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DarkVoiceFrame(modifier) {
        VoraAppBar("VORA Speaking", onBackClick, dark = true, trailing = { VoraStatusPill("SPEAKING", dark = true) })
        Spacer(Modifier.height(32.dp))
        VoraBigOrb(size = 200.dp)
        Spacer(Modifier.height(24.dp))
        // Tocar la tarjeta abre la conversacion completa.
        VoraDarkCard(modifier = Modifier.clip(RoundedCornerShape(20.dp)).clickable(onClick = onOpenConversationClick)) {
            Text(
                "\"That sounds like a balanced lunch. I've estimated around 520 calories with approximately 38 grams of protein.\"",
                color = Color.White,
                fontSize = 18.sp,
                lineHeight = 26.sp,
            )
        }
        Spacer(Modifier.height(24.dp))
        VoraProgressBar(0.35f, color = VoraMint, height = 3.dp)
        Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
            Text("0:08", color = Color.White.copy(alpha = 0.6f), fontSize = 11.sp, modifier = Modifier.weight(1f))
            Text("-0:14", color = Color.White.copy(alpha = 0.6f), fontSize = 11.sp)
        }
        Spacer(Modifier.height(20.dp))
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            RoundControl("⇄", 44)
            Surface(shape = CircleShape, color = VoraMint, modifier = Modifier.size(64.dp)) {
                Box(contentAlignment = Alignment.Center) { Text("❙❙", color = VoraGreen, fontSize = 18.sp) }
            }
            RoundControl("◎", 44)
        }
        Spacer(Modifier.height(24.dp))
        VoraLimeButton("Tap to Interrupt", onTapToInterruptClick)
    }
}

@Composable
private fun RoundControl(glyph: String, size: Int) {
    Surface(shape = CircleShape, color = Color.White.copy(alpha = 0.12f), modifier = Modifier.size(size.dp)) {
        Box(contentAlignment = Alignment.Center) { Text(glyph, color = Color.White.copy(alpha = 0.8f), fontSize = 18.sp) }
    }
}

/** Voice / 05 — Conversation. */
@Composable
fun VoiceConversationScreen(
    onBackClick: () -> Unit,
    onMicClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Surface(color = MaterialTheme.colorScheme.surface, shadowElevation = 0.dp, border = BorderStroke(1.dp, VoraBorder)) {
            VoraAppBar(
                "VORA Coach ●",
                onBackClick,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                trailing = { VoraOrb(size = 32.dp) },
            )
        }
        Column(
            modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text("TODAY AT 12:40 PM", fontSize = 11.sp, color = VoraTextMuted, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            UserBubble("What should I eat for dinner?", "12:41")
            CoachBubble(
                "You're currently 36g short of your protein target today. I'd highly recommend grilled chicken breast with roasted seasonal vegetables and a side of Greek yoghurt.",
                "12:42",
                withAudio = true,
            )
            UserBubble("Can you make that vegetarian?", "12:42")
            CoachBubble("Absolutely. Try grilled organic tofu with roasted vegetables, quinoa and Greek yoghurt.", "12:43")
        }
        // Barra de entrada (solo visual)
        Surface(color = MaterialTheme.colorScheme.surface, border = BorderStroke(1.dp, VoraBorder)) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Surface(shape = CircleShape, color = MaterialTheme.colorScheme.background, modifier = Modifier.size(40.dp)) {
                    Box(contentAlignment = Alignment.Center) { Text("+", fontSize = 20.sp, color = VoraTextMuted) }
                }
                Surface(shape = CircleShape, color = MaterialTheme.colorScheme.background, modifier = Modifier.weight(1f)) {
                    Row(Modifier.padding(horizontal = 16.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("Ask VORA anything...", color = VoraTextMuted, fontSize = 14.sp, modifier = Modifier.weight(1f))
                        Text("🎙", fontSize = 16.sp, modifier = Modifier.clickable(onClick = onMicClick))
                    }
                }
                Surface(shape = CircleShape, color = VoraGreen, modifier = Modifier.size(44.dp)) {
                    Box(contentAlignment = Alignment.Center) { Text("➤", color = Color.White, fontSize = 18.sp) }
                }
            }
        }
    }
}

@Composable
private fun UserBubble(text: String, time: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        Surface(shape = RoundedCornerShape(20.dp, 20.dp, 4.dp, 20.dp), color = VoraGreen, modifier = Modifier.widthIn(max = 280.dp)) {
            Column(Modifier.padding(horizontal = 18.dp, vertical = 14.dp)) {
                Text(text, color = Color.White, fontSize = 15.sp)
                Text(time, color = Color.White.copy(alpha = 0.6f), fontSize = 10.sp, modifier = Modifier.align(Alignment.End).padding(top = 6.dp))
            }
        }
    }
}

@Composable
private fun CoachBubble(text: String, time: String, withAudio: Boolean = false) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
        VoraOrb(size = 28.dp, modifier = Modifier.padding(top = 4.dp, end = 8.dp))
        Surface(
            shape = RoundedCornerShape(4.dp, 20.dp, 20.dp, 20.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, VoraBorder),
            modifier = Modifier.widthIn(max = 290.dp),
        ) {
            Column(Modifier.padding(horizontal = 18.dp, vertical = 14.dp)) {
                Text(text, fontSize = 15.sp, lineHeight = 22.sp)
                if (withAudio) {
                    Box(Modifier.padding(top = 12.dp).fillMaxWidth().height(1.dp).background(VoraBorder))
                    Text("▶  Listen to response (0:14)", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 10.dp))
                } else {
                    Text(time, color = VoraTextMuted, fontSize = 10.sp, modifier = Modifier.padding(top = 6.dp))
                }
            }
        }
    }
}

/** Voice / 06 — Error. */
@Composable
fun VoiceErrorScreen(
    onBackClick: () -> Unit,
    onTryAgainClick: () -> Unit,
    onTypeInsteadClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        VoraAppBar("System Alert", onBackClick)
        Spacer(Modifier.height(32.dp))
        VoraBigOrb(size = 170.dp, grey = true)
        Spacer(Modifier.height(24.dp))
        Text("Sorry, I didn't catch that", style = MaterialTheme.typography.headlineSmall)
        Text(
            "Try speaking again or type your request instead. Make sure your microphone is enabled.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp, start = 12.dp, end = 12.dp),
        )
        Spacer(Modifier.height(24.dp))
        VoraCard {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(shape = CircleShape, color = VoraError.copy(alpha = 0.12f), modifier = Modifier.size(32.dp)) {
                    Box(contentAlignment = Alignment.Center) { Text("🎙", fontSize = 14.sp) }
                }
                Text("Microphone access needed", style = MaterialTheme.typography.titleSmall, fontSize = 15.sp, modifier = Modifier.padding(start = 10.dp))
            }
            Text(
                "VORA uses your microphone to listen to your voice commands and provide real-time hands-free coaching.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            )
            // "Allow Access" / "Not Now" son solo visuales: el permiso real es de otra etapa.
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                VoraPrimaryButton("Allow Access", onClick = {}, modifier = Modifier.weight(1f))
                VoraOutlinedButton("Not Now", onClick = {}, modifier = Modifier.weight(1f))
            }
        }
        Spacer(Modifier.height(48.dp))
        Surface(shape = CircleShape, color = VoraMint, modifier = Modifier.fillMaxWidth().clip(CircleShape).clickable(onClick = onTryAgainClick)) {
            Text(
                "Try again",
                color = VoraGreen,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 16.dp),
            )
        }
        VoraTextLink("Type instead", onTypeInsteadClick, modifier = Modifier.padding(top = 12.dp), muted = true)
    }
}
