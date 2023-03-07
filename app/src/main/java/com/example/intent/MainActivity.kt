package com.example.intent

import android.bluetooth.BluetoothManager
import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.*
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.intent.audio.AndroidAudioPlayer
import com.example.intent.audio.AndroidAudioRecorder
import com.example.intent.audio.AudioRecordScreen
import com.example.intent.bluetooth.BluetoothViewModel
import com.example.intent.bluetooth.BluetoothScreen
import com.example.intent.call.CallScreen
import com.example.intent.pdf.PDFScreen
import com.example.intent.ui.theme.IntentTheme
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@SuppressLint("InlinedApi")
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val vibratorManager: Vibrator by lazy {
        getSystemService(VIBRATOR_SERVICE) as Vibrator
    }
    private val vibratorManagerService: VibratorManager by lazy {
        getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager
    }
    private val recorder by lazy {
        AndroidAudioRecorder(applicationContext)
    }

    private val player by lazy {
        AndroidAudioPlayer(applicationContext)
    }

    private var audioFile: File? = null

    private val bluetoothManager by lazy {
        applicationContext.getSystemService(BluetoothManager::class.java)
    }
    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter
    }
    private val isBluetoothEnabled: Boolean
        get() = bluetoothAdapter?.isEnabled == true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val enableBluetoothLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { /* Not needed */ }

        var canEnableBluetooth: Boolean? = null
        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            canEnableBluetooth = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                permissions[Manifest.permission.BLUETOOTH_CONNECT] == true
            } else true
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CALL_PHONE,
                )
            )
        } else {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CALL_PHONE,
                )
            )
        }

        fun vibrate(milliseconds: Long) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibratorManagerService.vibrate(
                    CombinedVibration.createParallel(
                        VibrationEffect.createOneShot(
                            milliseconds,
                            VibrationEffect.DEFAULT_AMPLITUDE
                        )
                    )
                )
            } else {
                vibratorManager.vibrate(milliseconds)
            }
        }
        setContent {
            IntentTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.fillMaxSize()
                ) {
                    val viewModel = hiltViewModel<BluetoothViewModel>()
                    val state by viewModel.state.collectAsState()

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        AudioRecordScreen(
                            onStartRecord = {
                                File(
                                    cacheDir,
                                    "audio.mp3"
                                ).also { recorder.start(it);audioFile = it }
                            },
                            onStopRecord = { recorder.stop() },
                            onPlayRecord = {
                                player.playFile(
                                    audioFile ?: return@AudioRecordScreen
                                )
                            },
                            onStopPlayerRecord = { player.stop() }
                        )
                        PDFScreen(this@MainActivity)
                        CallScreen(context = this@MainActivity)
                        BluetoothScreen(
                            state = state,
                            onStartScan = {
                                viewModel.startScan()
                                vibrate(100)
                                if (canEnableBluetooth!! && !isBluetoothEnabled) {
                                    enableBluetoothLauncher.launch(
                                        Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                                    )
                                }
                            },
                            onStopScan = viewModel::stopScan
                        )
                    }
                }
            }
        }
    }
}