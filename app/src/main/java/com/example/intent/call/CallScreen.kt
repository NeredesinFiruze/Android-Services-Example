package com.example.intent.call

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.lang.Exception

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CallScreen(context: Context) {
    var number by remember { mutableStateOf("") }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth(.8f)
                .padding(4.dp)
                .clip(RoundedCornerShape(12.dp))
                .height(50.dp)
                .border(
                    width = 1.dp,
                    brush = Brush.horizontalGradient(
                        listOf(
                            Color(0xFFBBFFA2),
                            Color(0xFF00FF06),
                        )
                    ),
                    shape = RoundedCornerShape(12.dp)
                ),
            value = number,
            onValueChange = { number = it },
            singleLine = true,
            maxLines = 1,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            shape = RoundedCornerShape(12.dp)
        )
        IconButton(
            modifier = Modifier
                .clip(CircleShape)
                .background(
                    brush = Brush.radialGradient(
                        listOf(
                            Color(0xFFBBFFA2),
                            Color(0xFF00FF06),
                        )
                    ),
                    shape = CircleShape
                ),
            onClick = {
                try {
                    val intent = Intent(Intent.ACTION_CALL)
                    intent.data = Uri.parse("tel:$number")
                    context.startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        ) {
            Icon(imageVector = Icons.Default.Call, contentDescription = null, tint = Color.White)
        }
    }
}