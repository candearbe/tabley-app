package com.uadeapps.tabley.domain.repository


import com.uadeapps.tabley.domain.model.TableColumn
import kotlinx.coroutines.flow.Flow

interface ColumnRepository {

    fun getColumnsByTable(tableId: Long): Flow<List<TableColumn>>

    suspend fun getColumnById(id: Long): TableColumn?

    suspend fun insertColumn(column: TableColumn): Long

    suspend fun updateColumn(column: TableColumn)

    suspend fun deleteColumn(column: TableColumn)
}
