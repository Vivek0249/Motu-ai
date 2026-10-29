package com.motu.ai.ui.screens

import androidx.lifecycle.ViewModel
import com.motu.ai.ui.components.QuickAction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class UiMessage(val fromUser: Boolean, val text: String)

data class HomeState(
    val listening: Boolean = false,
    val messages: List<UiMessage> = listOf(
        UiMessage(
            fromUser = false,
            text = "Namaste! I'm MOTU. This is the Stage 1 home screen. " +
                "Chat arrives in Stage 2 and voice in Stage 3.",
        ),
    ),
) {
    val statusLabel: String
        get() = if (listening) "Voice preview (microphone arrives in Stage 3)" else "Ready"
}

class HomeViewModel : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    fun toggleListening() {
        _state.update { it.copy(listening = !it.listening) }
    }

    fun submit(text: String) {
        val trimmed = text.trim()
        if (trimmed.isEmpty()) return
        _state.update {
            it.copy(
                messages = it.messages +
                    UiMessage(fromUser = true, text = trimmed) +
                    UiMessage(
                        fromUser = false,
                        text = "Gemini isn't connected yet. Real replies arrive in Stage 2.",
                    ),
            )
        }
    }

    fun onQuickAction(action: QuickAction) {
        if (action == QuickAction.Talk) {
            toggleListening()
            return
        }
        _state.update {
            it.copy(
                messages = it.messages +
                    UiMessage(fromUser = false, text = "${action.label} arrives in Stage ${action.stage}."),
            )
        }
    }
}
