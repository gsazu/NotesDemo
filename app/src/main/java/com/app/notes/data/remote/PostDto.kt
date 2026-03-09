package com.app.notes.data.remote

data class PostDto(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)
