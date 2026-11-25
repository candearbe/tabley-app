package com.uadeapps.tabley.domain.repository


import com.uadeapps.tabley.domain.model.Cell
import kotlinx.coroutines.flow.Flow

interface CellRepository {

    fun getCellsByRow(rowId: Long): Flow<List<Cell>>

    fun getCellsByColumn(columnId: Long): Flow<List<Cell>>

    suspend fun getCell(rowId: Long, columnId: Long): Cell?

    suspend fun insertCell(cell: Cell): Long

    suspend fun updateCell(cell: Cell)

    suspend fun deleteCell(cell: Cell)

    suspend fun deleteCellsByRow(rowId: Long)

    suspend fun deleteCellsByColumn(columnId: Long)
}
