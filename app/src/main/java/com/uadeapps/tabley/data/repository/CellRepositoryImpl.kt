package com.uadeapps.tabley.data.repository



import com.uadeapps.tabley.data.local.dao.CellDao
import com.uadeapps.tabley.data.local.entity.CellEntity
import com.uadeapps.tabley.domain.model.Cell
import com.uadeapps.tabley.domain.repository.CellRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CellRepositoryImpl @Inject constructor(
    private val cellDao: CellDao
) : CellRepository {

    override fun getCellsByRow(rowId: Long): Flow<List<Cell>> {
        return cellDao.getCellsByRow(rowId).map { cells ->
            cells.map { it.toDomain() }
        }
    }

    override fun getCellsByColumn(columnId: Long): Flow<List<Cell>> {
        return cellDao.getCellsByColumn(columnId).map { cells ->
            cells.map { it.toDomain() }
        }
    }

    override fun getCellsByTable(tableId: Long): Flow<List<Cell>> {
        return cellDao.getCellsByTable(tableId).map { cells ->
            cells.map { it.toDomain() }
        }
    }

    override suspend fun getCell(rowId: Long, columnId: Long): Cell? {
        return cellDao.getCell(rowId, columnId)?.toDomain()
    }

    override suspend fun insertCell(cell: Cell): Long {
        return cellDao.insertCell(cell.toEntity())
    }

    override suspend fun updateCell(cell: Cell) {
        cellDao.updateCell(cell.toEntity())
    }

    override suspend fun deleteCell(cell: Cell) {
        cellDao.deleteCell(cell.toEntity())
    }

    override suspend fun deleteCellsByRow(rowId: Long) {
        cellDao.deleteCellsByRow(rowId)
    }

    override suspend fun deleteCellsByColumn(columnId: Long) {
        cellDao.deleteCellsByColumn(columnId)
    }

    private fun CellEntity.toDomain(): Cell {
        return Cell(
            id = id,
            rowId = rowId,
            columnId = columnId,
            value = value,
            backgroundColor = color
        )
    }

    private fun Cell.toEntity(): CellEntity {
        return CellEntity(
            id = id,
            rowId = rowId,
            columnId = columnId,
            value = value,
            color = backgroundColor
        )
    }
}
