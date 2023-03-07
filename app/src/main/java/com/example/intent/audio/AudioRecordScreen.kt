package com.example.intent.audio

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun AudioRecordScreen(
    onStartRecord: () -> Unit,
    onStopRecord: () -> Unit,
    onPlayRecord: () -> Unit,
    onStopPlayerRecord: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(10.dp))
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Button(onClick = onStartRecord) {
                Text(text = "Start recording")
            }
            Button(onClick = onStopRecord) {
                Text(text = "Stop recording")
            }
            Button(onClick = onPlayRecord) {
                Text(text = "Play")
            }
            Button(onClick = onStopPlayerRecord) {
                Text(text = "Stop playing")
            }
        }
    }
}
