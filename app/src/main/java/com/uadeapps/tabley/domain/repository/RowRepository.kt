package com.uadeapps.tabley.domain.repository

import com.uadeapps.tabley.domain.model.TableRow
import kotlinx.coroutines.flow.Flow

interface RowRepository {

    fun getRowsByTable(tableId: Long): Flow<List<TableRow>>

    suspend fun getRowById(id: Long): TableRow?

    suspend fun insertRow(row: TableRow): Long

    suspend fun updateRow(row: TableRow)

    suspend fun deleteRow(row: TableRow)
}
