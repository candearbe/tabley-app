package com.uadeapps.tabley.domain.usecase.cell

import com.uadeapps.tabley.domain.model.Cell
import com.uadeapps.tabley.domain.repository.CellRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class SaveCellUseCaseTest {

    @Test
    fun `inserts a cell when the position is empty`() = runBlocking {
        val repository = FakeCellRepository()
        val useCase = SaveCellUseCase(repository)

        useCase(Cell(rowId = 1, columnId = 2, value = "UADE"))

        assertEquals(1, repository.insertCount)
        assertEquals(0, repository.updateCount)
        assertEquals("UADE", repository.storedCell?.value)
    }

    @Test
    fun `updates the existing cell instead of creating a duplicate`() = runBlocking {
        val repository = FakeCellRepository(
            initialCell = Cell(id = 10, rowId = 1, columnId = 2, value = "Antes")
        )
        val useCase = SaveCellUseCase(repository)

        useCase(Cell(rowId = 1, columnId = 2, value = "Después"))

        assertEquals(0, repository.insertCount)
        assertEquals(1, repository.updateCount)
        assertNotNull(repository.storedCell)
        assertEquals(10L, repository.storedCell?.id)
        assertEquals("Después", repository.storedCell?.value)
    }
}

private class FakeCellRepository(
    initialCell: Cell? = null
) : CellRepository {

    var storedCell: Cell? = initialCell
    var insertCount = 0
    var updateCount = 0

    override fun getCellsByRow(rowId: Long): Flow<List<Cell>> = emptyFlow()
    override fun getCellsByColumn(columnId: Long): Flow<List<Cell>> = emptyFlow()
    override fun getCellsByTable(tableId: Long): Flow<List<Cell>> = emptyFlow()

    override suspend fun getCell(rowId: Long, columnId: Long): Cell? =
        storedCell?.takeIf { it.rowId == rowId && it.columnId == columnId }

    override suspend fun insertCell(cell: Cell): Long {
        insertCount++
        storedCell = cell.copy(id = 1)
        return 1
    }

    override suspend fun updateCell(cell: Cell) {
        updateCount++
        storedCell = cell
    }

    override suspend fun deleteCell(cell: Cell) = Unit
    override suspend fun deleteCellsByRow(rowId: Long) = Unit
    override suspend fun deleteCellsByColumn(columnId: Long) = Unit
}
