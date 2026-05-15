package com.serhanensar.homeagentmobilek.ui.screens

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.serhanensar.homeagentmobilek.viewmodel.AssistantViewModel
import java.util.Locale

@Composable
fun AssistantScreen(
    viewModel: AssistantViewModel = viewModel()
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val responseText by viewModel.assistantResponseText.collectAsState()
    val context = LocalContext.current

    // Text To Speech Kurulumu
    var tts: TextToSpeech? by remember { mutableStateOf(null) }
    DisposableEffect(context) {
        val localTts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale("tr", "TR")
            }
        }
        tts = localTts
        onDispose {
            localTts.stop()
            localTts.shutdown()
        }
    }

    // TTS Yanıtını Oku
    LaunchedEffect(responseText) {
        if (!responseText.isNullOrBlank() && !isLoading) {
            tts?.speak(responseText, TextToSpeech.QUEUE_FLUSH, null, "AgentId")
        }
    }

    // Speech Recognizer Kurulumu
    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val spokenText = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull()
            if (!spokenText.isNullOrBlank()) {
                viewModel.processVoiceCommand(spokenText)
            }
        }
    }

    // Arayüz Görünümü
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "HomeAgent",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Evinizin yönetim ruhu",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Etkileşim Balonu (Chat Bubble)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            } else if (!responseText.isNullOrBlank()) {
                Text(
                    text = responseText!!,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 24.sp,
                    textAlign = TextAlign.Center
                )
            } else {
                Text(
                    text = "Dinlemeye hazırım...\nAşağıdaki butona dokunun.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Ortalanmış Çarpıcı Mikrofon Butonu
        FloatingActionButton(
            onClick = {
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE, "tr-TR")
                    putExtra(RecognizerIntent.EXTRA_PROMPT, "HomeAgent Sizi Dinliyor...")
                }
                try {
                    speechLauncher.launch(intent)
                } catch (e: Exception) {
                    viewModel.setResponse("Cihazınızda mikrofon desteği eksik.")
                }
            },
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(80.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Person, // Mikrofon yerinde Person ikonu kullanıldı (Derleme hatası engeli)
                contentDescription = "Dinle",
                tint = Color.White,
                modifier = Modifier.size(40.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
    }
}
