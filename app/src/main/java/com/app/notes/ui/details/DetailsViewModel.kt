package com.app.notes.ui.details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.notes.data.remote.TodoDto
import com.app.notes.data.repository.PostRepository
import com.app.notes.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: PostRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)
    val uiState: StateFlow<DetailsUiState> = _uiState.asStateFlow()

    init {
        // Retrieve the ID passed securely through Navigation
        val todoId = savedStateHandle.get<Int>("todoId")
        if (todoId != null) {
            fetchTodoDetails(todoId)
        } else {
            _uiState.value = DetailsUiState.Error("Invalid Todo ID")
        }
    }

    fun fetchTodoDetails(id: Int) {
        viewModelScope.launch {
            _uiState.value = DetailsUiState.Loading
            
            when (val result = repository.getTodoById(id)) {
                is Resource.Success -> {
                    _uiState.value = DetailsUiState.Success(result.data!!)
                }
                is Resource.Error -> {
                    Log.e("DetailsViewModel", "Error fetching details: ${result.message}")
                    _uiState.value = DetailsUiState.Error(result.message ?: "An unknown error occurred")
                }
                is Resource.Loading -> {
                     _uiState.value = DetailsUiState.Loading
                }
            }
        }
    }
}

sealed class DetailsUiState {
    object Loading : DetailsUiState()
    data class Success(val todo: TodoDto) : DetailsUiState()
    data class Error(val message: String) : DetailsUiState()
}
