package com.serhanensar.homeagentmobilek.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.serhanensar.homeagentmobilek.data.model.AssistantRequest
import com.serhanensar.homeagentmobilek.data.remote.HomeAgentService
import com.serhanensar.homeagentmobilek.util.PrefManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AssistantViewModel(application: Application) : AndroidViewModel(application) {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _assistantResponseText = MutableStateFlow<String?>(null)
    val assistantResponseText: StateFlow<String?> = _assistantResponseText

    private val prefManager = PrefManager(application)
    private var service: HomeAgentService? = null
    private var apiKey: String = ""

    init {
        rebuildRetrofit()
    }

    fun rebuildRetrofit() {
        val serverUrl = prefManager.getServerUrl() ?: "http://10.55.210.92:8000/"
        apiKey = prefManager.getApiKey() ?: ""
        
        try {
            val retrofit = Retrofit.Builder()
                .baseUrl(if (serverUrl.endsWith("/")) serverUrl else "$serverUrl/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            service = retrofit.create(HomeAgentService::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            service = null
        }
    }

    fun processVoiceCommand(prompt: String) {
        if (prompt.isBlank()) return
        
        _isLoading.value = true
        _assistantResponseText.value = null

        viewModelScope.launch {
            try {
                if (service == null) rebuildRetrofit()
                val request = AssistantRequest(prompt)
                val response = service?.askAssistant(apiKey, request)
                _assistantResponseText.value = response?.response ?: "Bağlantı hatası."
            } catch (e: Exception) {
                e.printStackTrace()
                _assistantResponseText.value = "Ağ veya bağlantı hatası."
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun setResponse(text: String?) {
        _assistantResponseText.value = text
    }
}
