package com.example.myapplication.model

import kotlinx.serialization.Serializable

//搜索的图片类
@Serializable
data class Resope(
    val statusCode: Int,
    val data: String
)