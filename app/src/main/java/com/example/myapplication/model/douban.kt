package com.example.myapplication.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable

//豆瓣的数据类
@Serializable
data class Douban(
    val subjects: List<Subject>
)

@Serializable
data class Subject(
    val episodes_info: String,
    val rate: String,
    val cover_x: Int,
    val title: String,
    val url: String,
    val playable: Boolean,
    val cover: String,
    val id: String,
    val cover_y: Int,
    val is_new: Boolean
)