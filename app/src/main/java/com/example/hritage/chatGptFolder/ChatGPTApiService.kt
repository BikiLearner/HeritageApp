package com.example.hritage.chatGptFolder

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ChatGPTApiService {
    @Headers(
        "Content-Type: application/json",
        "Authorization: Bearer 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"
    )
    @POST("engines/davinci-codex/completions")
    fun getChatGPTResponse(@Body request: ChatGPTRequest): Call<ChatGPTResponse>
}
