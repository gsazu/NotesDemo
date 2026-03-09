package com.app.notes.data.remote

/**
 * A fake DTO class to serve as an example of how to map a JSON response.
 */
data class FakeDto(
    val id: Int,
    val name: String,
    val description: String? = null
)
