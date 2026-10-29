package com.motu.ai.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class QuickAction(val emoji: String, val label: String, val stage: Int) {
    Talk("🎙", "Talk to MOTU", 3),
    Type("⌨", "Type a message", 2),
    Apps("📱", "Open Apps", 4),
    Music("🎵", "Play Music", 4),
    Call("📞", "Call Contact", 5),
    Message("💬", "Send Message", 5),
    Code("💻", "Coding Assistant", 7),
    Screen("👁", "Screen Assistant", 6),
    Settings("⚙", "Settings", 8),
}

@Composable
fun ActionTile(action: QuickAction, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(16.dp)
    Column(
        modifier = modifier
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
            .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f), shape)
            .clickable(onClickLabel = action.label, onClick = onClick)
            .padding(vertical = 14.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = action.emoji, fontSize = 24.sp)
        Spacer(Modifier.height(6.dp))
        Text(
            text = action.label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            maxLines = 2,
        )
    }
}
