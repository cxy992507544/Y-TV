package com.example.myapplication.model
import kotlinx.serialization.Serializable


@Serializable
data class MovieDetails(
    val title: String,
    val description: String,
    val actors: String,
    val posterUrl: String
)