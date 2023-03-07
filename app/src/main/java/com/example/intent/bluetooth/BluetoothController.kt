package com.example.intent.bluetooth

import kotlinx.coroutines.flow.StateFlow

interface BluetoothController {
    val scannedDevices: StateFlow<List<DeviceState>>
    val pairedDevices: StateFlow<List<DeviceState>>

    fun startDiscovery()
    fun stopDiscovery()
    fun release()
}