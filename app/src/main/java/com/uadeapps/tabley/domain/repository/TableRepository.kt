package com.uadeapps.tabley.domain.repository


import com.uadeapps.tabley.domain.model.TableSheet
import kotlinx.coroutines.flow.Flow

interface TableRepository {

    fun getTablesByNotebook(notebookId: Long): Flow<List<TableSheet>>

    suspend fun getTableById(id: Long): TableSheet?

    suspend fun insertTable(table: TableSheet): Long

    suspend fun updateTable(table: TableSheet)

    suspend fun deleteTable(table: TableSheet)
}
