package com.example.myapplication.network

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class OkHttp {
    private val client = OkHttpClient()

    // 封装 GET 请求
    suspend fun get(url: String): String? = withContext(Dispatchers.IO) {
        val request = Request.Builder().url(url).build()
        val call = client.newCall(request)

        try {
            call.execute().use { response ->
                if (response.isSuccessful) {
                    response.body?.string()
                } else {
                    println("Error: Unexpected code ${response.code}")
                    null
                }
            }
        } catch (e: Exception) {
            println("Error: ${e.message}")
            null
        }
    }

    // 封装 POST 请求
    suspend fun post(url: String, json: String): String? = withContext(Dispatchers.IO) {
        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val requestBody = json.toRequestBody(mediaType)
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        try {
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    response.body?.string()
                } else {
                    println("Error: Unexpected code ${response.code}")
                    null
                }
            }
        } catch (e: Exception) {
            println("Error: ${e.message}")
            null
        }
    }
}
