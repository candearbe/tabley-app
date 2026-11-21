package com.uadeapps.tabley.domain.repository


import com.uadeapps.tabley.domain.model.Notebook
import kotlinx.coroutines.flow.Flow

interface NotebookRepository {

    fun getAllNotebooks(): Flow<List<Notebook>>

    suspend fun getNotebookById(id: Long): Notebook?

    suspend fun insertNotebook(notebook: Notebook): Long

    suspend fun updateNotebook(notebook: Notebook)

    suspend fun deleteNotebook(notebook: Notebook)
}
