package com.uadeapps.tabley.presentation.notebook_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uadeapps.tabley.domain.usecase.table.CreateTableUseCase
import com.uadeapps.tabley.domain.usecase.table.GetTablesByNotebookUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotebookDetailViewModel @Inject constructor(
    private val getTablesByNotebookUseCase: GetTablesByNotebookUseCase,
    private val createTableUseCase: CreateTableUseCase
) : ViewModel() {

    fun getTables(notebookId: Long) =
        getTablesByNotebookUseCase(notebookId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    fun createTable(notebookId: Long, name: String) {
        viewModelScope.launch {
            createTableUseCase(notebookId, name)
        }
    }
}