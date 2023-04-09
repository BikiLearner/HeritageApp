package com.example.hritage.chatGptFolder

data class ChatGPTResponse(
    val id: String,
    val object1: String,
    val choices: List<ChatGPTChoice>
)
