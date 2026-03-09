package com.app.notes.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.notes.data.remote.FakeDto
import com.app.notes.data.remote.PostDto
import com.app.notes.data.repository.PostRepository
import com.app.notes.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: PostRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        fetchPosts()
        // Here is how you would call the fake function:
        // fetchFakeData()
    }

    fun fetchPosts() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            
            // Using the Result wrapper handled by safeApiCall in the Repository
            when (val result = repository.getPosts()) {
                is Resource.Success -> {
                    _uiState.value = HomeUiState.Success(result.data ?: emptyList())
                }
                is Resource.Error -> {
                    Log.e("HomeViewModel", "Error fetching posts: ${result.message}")
                    _uiState.value = HomeUiState.Error(result.message ?: "An unknown error occurred")
                }
                is Resource.Loading -> {
                     _uiState.value = HomeUiState.Loading
                }
            }
        }
    }

    /**
     * FAKE VIEWMODEL CONSUMPTION EXAMPLE:
     * Shows how to consume the SafeApiCall `Resource` natively in the ViewModel.
     */
    fun fetchFakeData() {
        viewModelScope.launch {
            // 1. You could update a separate loading state here
            Log.d("HomeViewModel", "Fetching fake data...")

            // 2. Make the repository call
            when (val result = repository.getFakeData()) {
                is Resource.Success -> {
                    // 3a. IT WORKED! Do something with `result.data`
                    val fakeDto = result.data
                    Log.d("HomeViewModel", "Got Fake Data: ${fakeDto?.name}")
                }
                is Resource.Error -> {
                    // 3b. IT FAILED (No internet, HTTP 404/500, etc)
                    val errorMessage = result.message
                    Log.e("HomeViewModel", "Fake request failed: $errorMessage")
                }
                is Resource.Loading -> {
                    // Usually unused since UI loading state is handled before the request
                }
            }
        }
    }
}

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val posts: List<PostDto>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}
