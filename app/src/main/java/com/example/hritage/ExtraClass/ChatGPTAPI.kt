package com.example.hritage.ExtraClass

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class ChatGPTAPI(private val apiKey: String) {
    private val client = OkHttpClient()

    fun getResponse(input: String): String {
        val mediaType = "application/json".toMediaType()
        val requestBody = """
            {
                "model": "text-davinci-002",
                "prompt": "$input",
                "temperature": 0.5,
                "max_tokens": 50,
                "n": 1,
                "stop": ["\n"]
            }
        """.trimIndent().toRequestBody(mediaType)

        val request = Request.Builder()
            .url("https://api.openai.com/v1/engine/complete")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer $apiKey")
            .post(requestBody)
            .build()

        val response = client.newCall(request).execute()
        return response.body?.string() ?: ""
    }
}