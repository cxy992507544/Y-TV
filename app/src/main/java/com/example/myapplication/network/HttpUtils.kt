package com.example.myapplication.network

import android.os.AsyncTask
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets

class HttpUtils {

    companion object {
        private const val READ_TIMEOUT = 15000 // 15 seconds
        private const val CONNECT_TIMEOUT = 15000 // 15 seconds
    }

    // 同步 GET 请求
    @Throws(Exception::class)
    fun Get(url: String): String? {
//        return GetRequestTask().execute(url).get()
        return doInBackground(url)
    }


    // 同步 POST 请求
    @Throws(Exception::class)
    fun Post(url: String, json: String): String? {
        return PostRequestTask().execute(url, json).get()
    }

    // GET 请求任务
    private class GetRequestTask : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String): String? {
            val url = params[0]
            return try {
                with(URL(url).openConnection() as HttpURLConnection) {
                    requestMethod = "GET"
                    readTimeout = READ_TIMEOUT
                    connectTimeout = CONNECT_TIMEOUT

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader(InputStreamReader(inputStream)).use { reader ->
                            val response = StringBuilder()
                            var line: String?
                            while (reader.readLine().also { line = it } != null) {
                                response.append(line)
                            }
                            response.toString()
                        }
                    } else {
                        errorStream.bufferedReader().use { reader ->
                            val errorResponse = StringBuilder()
                            var line: String?
                            while (reader.readLine().also { line = it } != null) {
                                errorResponse.append(line)
                            }
                            throw IOException("HTTP error code: ${responseCode}. Response: $errorResponse")
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    fun doInBackground(vararg params: String): String? {
        val url = params[0]
        return try {
            with(URL(url).openConnection() as HttpURLConnection) {
                requestMethod = "GET"
                readTimeout = READ_TIMEOUT
                connectTimeout = CONNECT_TIMEOUT
                setRequestProperty("User-Agent", "Mozilla/5.0")

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader(InputStreamReader(inputStream)).use { reader ->
                        val response = StringBuilder()
                        var line: String?
                        while (reader.readLine().also { line = it } != null) {
                            response.append(line)
                        }
                        response.toString().also { println("Response: $it") } // 打印响应
                    }
                } else {
                    errorStream.bufferedReader().use { reader ->
                        val errorResponse = StringBuilder()
                        var line: String?
                        while (reader.readLine().also { line = it } != null) {
                            errorResponse.append(line)
                        }
                        throw IOException("HTTP error code: ${responseCode}. Response: $errorResponse")
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // POST 请求任务
    private class PostRequestTask : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String): String? {
            val url = params[0]
            val json = params[1]
            return try {
                with(URL(url).openConnection() as HttpURLConnection) {
                    requestMethod = "POST"
                    setRequestProperty("Content-Type", "application/json; charset=UTF-8")
                    doOutput = true
                    readTimeout = READ_TIMEOUT
                    connectTimeout = CONNECT_TIMEOUT

                    val outputStream: OutputStream = this.outputStream
                    outputStream.write(json.toByteArray(StandardCharsets.UTF_8))
                    outputStream.flush()
                    outputStream.close()

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader(InputStreamReader(inputStream)).use { reader ->
                            val response = StringBuilder()
                            var line: String?
                            while (reader.readLine().also { line = it } != null) {
                                response.append(line)
                            }
                            response.toString()
                        }
                    } else {
                        errorStream.bufferedReader().use { reader ->
                            val errorResponse = StringBuilder()
                            var line: String?
                            while (reader.readLine().also { line = it } != null) {
                                errorResponse.append(line)
                            }
                            throw IOException("HTTP error code: ${responseCode}. Response: $errorResponse")
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}