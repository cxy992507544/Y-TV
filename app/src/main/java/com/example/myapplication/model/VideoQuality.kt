package com.example.myapplication.model
import kotlinx.serialization.Serializable

@Serializable
data class VideoQuality(
    val name: String? = "",
    val url: String? = ""
)
