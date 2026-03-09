package com.app.notes.data.repository

import com.app.notes.data.remote.ApiService
import com.app.notes.data.remote.FakeDto
import com.app.notes.data.remote.PostDto
import com.app.notes.data.remote.TodoDto
import com.app.notes.util.Resource
import com.app.notes.util.safeApiCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getPosts(): Resource<List<PostDto>> {
        return safeApiCall { apiService.getPosts() }
    }

    suspend fun getTodoById(id: Int): Resource<TodoDto> {
        return safeApiCall { apiService.getTodoById(id) }
    }

    suspend fun getFakeData(): Resource<FakeDto> {
        return safeApiCall { apiService.getFakeData() }
    }
}
