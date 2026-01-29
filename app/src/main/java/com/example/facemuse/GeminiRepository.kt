package com.example.facemuse

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GeminiRepository {

    // Using the free tier API Key.
    // Ensure this key is valid and has the Gemini API enabled in Google AI Studio.
    private val apiKey = "AIzaSyCklURekNzGXXJyQcBeE6N78rb9_lm7cZo"

    private val api = Retrofit.Builder()
        .baseUrl("https://generativelanguage.googleapis.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GeminiApi::class.java)

    suspend fun getAIResponse(prompt: String): String {
        return try {
            val request = GeminiRequest(
                contents = listOf(
                    Content(listOf(Part(prompt)))
                )
            )
            
            val response = api.generate(apiKey, request)
            
            val candidate = response.candidates?.firstOrNull()
            val text = candidate?.content?.parts?.firstOrNull()?.text
            
            text ?: "The AI returned an empty response."
            
        } catch (e: Exception) {
            android.util.Log.e("GeminiError", "API Call Failed", e)
            "AI service unavailable. Please check your connection or try again later. (${e.message})"
        }
    }
}
