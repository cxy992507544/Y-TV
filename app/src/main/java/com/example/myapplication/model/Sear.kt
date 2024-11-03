package com.example.myapplication.model

import kotlinx.serialization.Serializable

@Serializable
data class Sear(
    val id: String,
    val url: String,
    val author: String,
    val cover: String,
    val desc: String,
    val list: String,
    val rquestId: String,
    val tags: String,
    val title: String,
    val detailUrl: String
)

@Serializable
data class SearList(
    val data: List<Sear>
)
