package com.motu.ai.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.motu.ai.device.readBattery
import com.motu.ai.ui.components.ActionTile
import com.motu.ai.ui.components.AiCore
import com.motu.ai.ui.components.MessageBubble
import com.motu.ai.ui.components.QuickAction
import com.motu.ai.ui.components.Waveform
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var now by remember { mutableStateOf(LocalDateTime.now()) }
    var battery by remember { mutableStateOf(readBattery(context)) }

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            while (true) {
                now = LocalDateTime.now()
                battery = readBattery(context)
                delay(15_000)
            }
        }
    }

    val timeFormat = remember { DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault()) }
    val dateFormat = remember { DateTimeFormatter.ofPattern("EEEE, d MMMM", Locale.getDefault()) }
    var draft by rememberSaveable { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(Color(0xFF020409), Color(0xFF071225))),
            ),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 640.dp)
                .fillMaxWidth()
                .safeDrawingPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = now.format(timeFormat),
                        fontSize = 28.sp,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = now.format(dateFormat),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                battery?.let {
                    Text(
                        text = "🔋 ${it.percent}%" + if (it.charging) " ⚡" else "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            AiCore(
                active = state.listening,
                modifier = Modifier
                    .fillMaxWidth(0.65f)
                    .widthIn(max = 280.dp),
            )

            Spacer(Modifier.height(8.dp))
            Text(
                text = "MOTU",
                fontSize = 22.sp,
                fontFamily = FontFamily.Monospace,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = state.statusLabel,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(Modifier.height(8.dp))
            Waveform(
                active = state.listening,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
            )

            Spacer(Modifier.height(8.dp))
            MicButton(listening = state.listening, onClick = viewModel::toggleListening)

            Spacer(Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                state.messages.takeLast(6).forEach {
                    MessageBubble(text = it.text, fromUser = it.fromUser)
                }
            }

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = draft,
                onValueChange = { draft = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Type a message") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(
                    onSend = {
                        viewModel.submit(draft)
                        draft = ""
                    },
                ),
                trailingIcon = {
                    TextButton(
                        onClick = {
                            viewModel.submit(draft)
                            draft = ""
                        },
                    ) { Text("Send") }
                },
            )

            Spacer(Modifier.height(16.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                QuickAction.entries.chunked(3).forEach { rowActions ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        rowActions.forEach { action ->
                            ActionTile(
                                action = action,
                                onClick = { viewModel.onQuickAction(action) },
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun MicButton(listening: Boolean, onClick: () -> Unit) {
    val primary = MaterialTheme.colorScheme.primary
    Box(
        modifier = Modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(if (listening) primary.copy(alpha = 0.85f) else MaterialTheme.colorScheme.surfaceVariant)
            .border(2.dp, primary.copy(alpha = 0.6f), CircleShape)
            .clickable(
                onClickLabel = if (listening) "Stop listening" else "Start listening",
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "🎙", fontSize = 30.sp)
    }
}
