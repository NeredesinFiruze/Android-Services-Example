package com.example.intent.bluetooth

data class UiState(
    val scannedDevices: List<DeviceState> = emptyList(),
    val pairedDevices: List<DeviceState> = emptyList(),
)