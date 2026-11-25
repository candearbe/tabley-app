package com.uadeapps.tabley.presentation.notebooks


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uadeapps.tabley.domain.model.Notebook
import com.uadeapps.tabley.domain.usecase.notebook.CreateNotebookUseCase
import com.uadeapps.tabley.domain.usecase.notebook.DeleteNotebookUseCase
import com.uadeapps.tabley.domain.usecase.notebook.GetAllNotebooksUseCase
import com.uadeapps.tabley.domain.usecase.notebook.UpdateNotebookUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotebookViewModel @Inject constructor(
    private val getAllNotebooksUseCase: GetAllNotebooksUseCase,
    private val createNotebookUseCase: CreateNotebookUseCase,
    private val updateNotebookUseCase: UpdateNotebookUseCase,
    private val deleteNotebookUseCase: DeleteNotebookUseCase
) : ViewModel() {

    val notebooks = getAllNotebooksUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun createNotebook(name: String, color: String) {
        viewModelScope.launch {
            createNotebookUseCase(name, color)
        }
    }

    fun updateNotebook(notebook: Notebook) {
        viewModelScope.launch {
            updateNotebookUseCase(notebook)
        }
    }

    fun deleteNotebook(notebook: Notebook) {
        viewModelScope.launch {
            deleteNotebookUseCase(notebook)
        }
    }
}
