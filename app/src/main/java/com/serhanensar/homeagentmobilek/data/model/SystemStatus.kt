package com.serhanensar.homeagentmobilek.data.model

import com.google.gson.annotations.SerializedName

data class SystemStatus(
    @SerializedName("cpu_percent") val cpuPercent: Double,
    @SerializedName("ram_percent") val ramPercent: Double,
    @SerializedName("disk_percent") val diskPercent: Double,
    @SerializedName("cpu_temp") val cpuTemp: Double
)
