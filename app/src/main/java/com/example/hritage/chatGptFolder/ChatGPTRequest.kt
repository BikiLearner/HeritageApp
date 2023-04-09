package com.example.hritage.chatGptFolder

data class ChatGPTRequest(
    val prompt: String,
    val max_tokens: Int,
    val temperature: Double
)
