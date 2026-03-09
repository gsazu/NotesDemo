package com.app.notes.data.remote

import retrofit2.http.GET
import retrofit2.http.Headers

interface ApiService {

    // Example 1: Endpoint WITHOUT authentication (Public API)
    @GET("posts")
    suspend fun getPosts(): List<PostDto>

    // Example 2: Get specific Todo WITHOUT authentication (Public API)
    @GET("todos/{id}")
    suspend fun getTodoById(@retrofit2.http.Path("id") id: Int): TodoDto

    /**
     * FAKE API ENDPOINT EXAMPLE:
     * This endpoint REQUIRES authentication.
     * By adding `@Headers("Requires-Auth: true")`, our AuthInterceptor
     * will automatically attach the "Authorization: Bearer <token>" to it.
     */
    @Headers("Requires-Auth: true")
    @GET("fake/endpoint/path")
    suspend fun getFakeData(): FakeDto
}
