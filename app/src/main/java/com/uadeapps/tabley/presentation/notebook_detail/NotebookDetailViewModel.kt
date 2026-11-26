package com.uadeapps.tabley.presentation.notebook_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uadeapps.tabley.domain.model.TableSheet
import com.uadeapps.tabley.domain.usecase.table.CreateTableUseCase
import com.uadeapps.tabley.domain.usecase.table.GetTablesByNotebookUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NotebookDetailUiState(
    val isLoading: Boolean = true,
    val tables: List<TableSheet> = emptyList(),
    val errorMessage: String? = null
)

@HiltViewModel
class NotebookDetailViewModel @Inject constructor(
    private val getTablesByNotebookUseCase: GetTablesByNotebookUseCase,
    private val createTableUseCase: CreateTableUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotebookDetailUiState())
    val uiState: StateFlow<NotebookDetailUiState> = _uiState.asStateFlow()

    private var currentNotebookId: Long? = null
    private var observationJob: Job? = null

    fun loadNotebook(notebookId: Long) {
        if (currentNotebookId == notebookId && observationJob?.isActive == true) return

        currentNotebookId = notebookId
        observationJob?.cancel()
        _uiState.value = NotebookDetailUiState(isLoading = true)

        observationJob = viewModelScope.launch {
            getTablesByNotebookUseCase(notebookId)
                .catch { throwable ->
                    _uiState.value = NotebookDetailUiState(
                        isLoading = false,
                        errorMessage = throwable.message
                            ?: "No se pudieron cargar las tablas"
                    )
                }
                .collect { tables ->
                    _uiState.value = NotebookDetailUiState(
                        isLoading = false,
                        tables = tables
                    )
                }
        }
    }

    fun createTable(name: String) {
        val notebookId = currentNotebookId ?: return
        viewModelScope.launch {
            runCatching { createTableUseCase(notebookId, name.trim()) }
                .onFailure { throwable ->
                    _uiState.value = _uiState.value.copy(
                        errorMessage = throwable.message
                            ?: "No se pudo crear la tabla"
                    )
                }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
