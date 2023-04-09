package com.example.hritage.model

data class CompletionRequest(
    val prompt: String,
    val maxTokens: Int = 150,
    val temperature: Double = 0.7
)

data class CompletionResponse(
    val choices: List<Choice>
)

data class Choice(
    val text: String,
    val index: Int,
    val logprobs: Logprobs,
    val finish_reason: String
)

data class Logprobs(
    val token_logprobs: List<Double>,
    val top_logprobs: List<Double>,
    val text_offset: List<Int>
)
