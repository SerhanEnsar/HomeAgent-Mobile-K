// Copyright (c) 2026 Serhan Ensar. All rights reserved.
package com.serhanensar.homeagentmobilek.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.serhanensar.homeagentmobilek.ui.theme.AccentCyan
import com.serhanensar.homeagentmobilek.ui.theme.BackgroundDark
import com.serhanensar.homeagentmobilek.util.PrefManager

val CardDark = Color(0xFF2A2A2A)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val prefManager = remember { PrefManager(context) }

    var serverUrl by remember { mutableStateOf(prefManager.getServerUrl() ?: "") }
    var apiKey by remember { mutableStateOf(prefManager.getApiKey() ?: "") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Sistem Bağlantı Ayarları",
            color = AccentCyan,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "HomeAgent veya Raspberry Pi sunucunuzun IP adresi ve yetkilendirme anahtarını aşağıdan yapılandırabilirsiniz.",
            color = Color.Gray,
            fontSize = 14.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // URL Input
        OutlinedTextField(
            value = serverUrl,
            onValueChange = { serverUrl = it },
            label = { Text("Sunucu URL / IP Adresi") },
            leadingIcon = { Icon(Icons.Filled.Info, contentDescription = "URL") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AccentCyan,
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = AccentCyan,
                cursorColor = AccentCyan,
                focusedContainerColor = CardDark,
                unfocusedContainerColor = CardDark,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.LightGray
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // API Key Input
        OutlinedTextField(
            value = apiKey,
            onValueChange = { apiKey = it },
            label = { Text("API Güvenlik Anahtarı (Key)") },
            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "API Key") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AccentCyan,
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = AccentCyan,
                cursorColor = AccentCyan,
                focusedContainerColor = CardDark,
                unfocusedContainerColor = CardDark,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.LightGray
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = {
                if (serverUrl.isBlank() || apiKey.isBlank()) {
                    Toast.makeText(context, "Lütfen tüm alanları doldurun.", Toast.LENGTH_SHORT).show()
                } else {
                    prefManager.saveServerUrl(serverUrl)
                    prefManager.saveApiKey(apiKey)
                    Toast.makeText(context, "Bağlantı ayarları şifrelenerek kaydedildi!", Toast.LENGTH_LONG).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AccentCyan)
        ) {
            Text(
                text = "AYARLARI KAYDET",
                color = BackgroundDark,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Not: Kayıt işleminden sonra Dashboard veya Asistan sekmesinde yeni adrese anında bağlanılacaktır.",
            color = Color.DarkGray,
            fontSize = 12.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}
