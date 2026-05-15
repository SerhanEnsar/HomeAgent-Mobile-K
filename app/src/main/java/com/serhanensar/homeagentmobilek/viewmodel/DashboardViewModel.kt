package com.serhanensar.homeagentmobilek.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.serhanensar.homeagentmobilek.data.model.SystemStatus
import com.serhanensar.homeagentmobilek.data.remote.HomeAgentService
import com.serhanensar.homeagentmobilek.util.PrefManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

enum class ConnectionStatus {
    CONNECTING, CONNECTED, DISCONNECTED
}

class DashboardViewModel(application: Application) : AndroidViewModel(application) {
    private val _status = MutableStateFlow<SystemStatus?>(null)
    val status: StateFlow<SystemStatus?> = _status

    private val _connectionStatus = MutableStateFlow(ConnectionStatus.CONNECTING)
    val connectionStatus: StateFlow<ConnectionStatus> = _connectionStatus

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private val prefManager = PrefManager(application)
    private var service: HomeAgentService? = null
    private var apiKey: String = ""

    init {
        rebuildRetrofit()
        startPolling()
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
            _connectionStatus.value = ConnectionStatus.DISCONNECTED
        }
    }

    private fun startPolling() {
        viewModelScope.launch {
            while (true) {
                if (!_isRefreshing.value) {
                    try {
                        if (service == null) rebuildRetrofit()
                        val result = service?.getStatus(apiKey)
                        if (result != null) {
                            _status.value = result
                            _connectionStatus.value = ConnectionStatus.CONNECTED
                        } else {
                            _connectionStatus.value = ConnectionStatus.DISCONNECTED
                        }
                    } catch (e: Exception) {
                        _connectionStatus.value = ConnectionStatus.DISCONNECTED
                        e.printStackTrace()
                    }
                }
                delay(3000)
            }
        }
    }

    fun refresh() {
        if (_isRefreshing.value) return
        viewModelScope.launch {
            _isRefreshing.value = true
            _connectionStatus.value = ConnectionStatus.CONNECTING
            rebuildRetrofit()
            try {
                val result = service?.getStatus(apiKey)
                if (result != null) {
                    _status.value = result
                    _connectionStatus.value = ConnectionStatus.CONNECTED
                } else {
                    _connectionStatus.value = ConnectionStatus.DISCONNECTED
                }
            } catch (e: Exception) {
                _connectionStatus.value = ConnectionStatus.DISCONNECTED
                e.printStackTrace()
            } finally {
                _isRefreshing.value = false
            }
        }
    }
}
