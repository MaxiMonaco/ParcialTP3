package com.example.parcialtp3.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.parcialtp3.ui.voice.VoiceConversationScreen
import com.example.parcialtp3.ui.voice.VoiceErrorScreen
import com.example.parcialtp3.ui.voice.VoiceIdleScreen
import com.example.parcialtp3.ui.voice.VoiceListeningScreen
import com.example.parcialtp3.ui.voice.VoiceProcessingScreen
import com.example.parcialtp3.ui.voice.VoiceSpeakingScreen

/**
 * AI Voice Flow (Asistente VORA): 6 estados que se recorren con datos simulados.
 * No es una pestana: se entra desde Home, Workout y Progress, y "atras" vuelve a quien lo abrio.
 * "Cancel" y "Try again" regresan a Idle sin apilar pantallas repetidas.
 */
fun NavGraphBuilder.voiceGraph(navController: NavController) {
    navigation(
        startDestination = AppDestination.VoiceIdle.route,
        route = AppDestination.VoiceFlow.route,
    ) {
        composable(AppDestination.VoiceIdle.route) {
            VoiceIdleScreen(
                onBackClick = { navController.popBackStack() },
                onTapToTalkClick = { navController.navigate(AppDestination.VoiceListening.route) },
                onTypeInsteadClick = { navController.navigate(AppDestination.VoiceConversation.route) },
                onSuggestedQuestionClick = { navController.navigate(AppDestination.VoiceConversation.route) },
                onSimulateErrorClick = { navController.navigate(AppDestination.VoiceError.route) },
            )
        }
        composable(AppDestination.VoiceListening.route) {
            VoiceListeningScreen(
                onBackClick = { navController.popBackStack() },
                onCancelClick = { navController.returnToVoiceIdle() },
                onFinishedListeningClick = { navController.navigate(AppDestination.VoiceProcessing.route) },
            )
        }
        composable(AppDestination.VoiceProcessing.route) {
            VoiceProcessingScreen(
                onBackClick = { navController.popBackStack() },
                onCancelClick = { navController.returnToVoiceIdle() },
                onFinishedProcessingClick = { navController.navigate(AppDestination.VoiceSpeaking.route) },
            )
        }
        composable(AppDestination.VoiceSpeaking.route) {
            VoiceSpeakingScreen(
                onBackClick = { navController.popBackStack() },
                onTapToInterruptClick = { navController.navigate(AppDestination.VoiceListening.route) },
                onOpenConversationClick = { navController.navigate(AppDestination.VoiceConversation.route) },
            )
        }
        composable(AppDestination.VoiceConversation.route) {
            VoiceConversationScreen(
                onBackClick = { navController.popBackStack() },
                onMicClick = { navController.navigate(AppDestination.VoiceListening.route) },
            )
        }
        composable(AppDestination.VoiceError.route) {
            VoiceErrorScreen(
                onBackClick = { navController.popBackStack() },
                onTryAgainClick = { navController.returnToVoiceIdle() },
                onTypeInsteadClick = { navController.navigate(AppDestination.VoiceConversation.route) },
            )
        }
    }
}
