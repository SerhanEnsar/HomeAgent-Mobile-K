// Copyright (c) 2026 Serhan Ensar. All rights reserved.
package com.serhanensar.homeagentmobilek.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.serhanensar.homeagentmobilek.ui.components.MetricCard
import com.serhanensar.homeagentmobilek.ui.theme.AccentCyan
import com.serhanensar.homeagentmobilek.ui.theme.BackgroundDark
import com.serhanensar.homeagentmobilek.ui.theme.TextGray
import com.serhanensar.homeagentmobilek.viewmodel.ConnectionStatus
import com.serhanensar.homeagentmobilek.viewmodel.DashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(viewModel: DashboardViewModel = viewModel()) {
    val status by viewModel.status.collectAsState()
    val connectionStatus by viewModel.connectionStatus.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val pullRefreshState = rememberPullToRefreshState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "HomeAgent",
                            color = AccentCyan,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            when (connectionStatus) {
                                ConnectionStatus.CONNECTING -> {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(12.dp),
                                        strokeWidth = 2.dp,
                                        color = AccentCyan
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Bağlanılıyor...", fontSize = 12.sp, color = TextGray)
                                }
                                ConnectionStatus.CONNECTED -> {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .padding(top = 2.dp)
                                            .background(Color.Green, shape = androidx.compose.foundation.shape.CircleShape)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Bağlanıldı", fontSize = 12.sp, color = Color.Green)
                                }
                                ConnectionStatus.DISCONNECTED -> {
                                    Text("Bağlantı kesildi!", fontSize = 12.sp, color = Color.Red)
                                }
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundDark
                )
            )
        },
        containerColor = BackgroundDark
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { viewModel.refresh() },
            state = pullRefreshState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 8.dp)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                item {
                    MetricCard(
                        label = "CPU Kullanımı",
                        value = status?.let { "${it.cpuPercent}%" } ?: "0.0%",
                        percent = status?.let { (it.cpuPercent / 100).toFloat() } ?: 0f
                    )
                    MetricCard(
                        label = "RAM Kullanımı",
                        value = status?.let { "${it.ramPercent}%" } ?: "0.0%",
                        percent = status?.let { (it.ramPercent / 100).toFloat() } ?: 0f
                    )
                    MetricCard(
                        label = "Disk Kullanımı",
                        value = status?.let { "${it.diskPercent}%" } ?: "0.0%",
                        percent = status?.let { (it.diskPercent / 100).toFloat() } ?: 0f
                    )
                    MetricCard(
                        label = "Sıcaklık",
                        value = status?.let { "${it.cpuTemp}°C" } ?: "0.0°C",
                        percent = status?.let { (it.cpuTemp / 100).coerceIn(0.0, 1.0).toFloat() } ?: 0f,
                        isTemperature = true,
                        tempValue = status?.cpuTemp ?: 0.0
                    )
                }
            }
        }
    }
}
