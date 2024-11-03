package com.example.myapplication.model

import kotlinx.serialization.Serializable

@Serializable
data class MediaSource(
    val name: String,
    val rquestId: String,
    val items: List<Item>
)

@Serializable
data class Item(
    val url: String,
    val title: String,
    val rquestId: String,
    var desc: String = ""
)
