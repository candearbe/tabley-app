package com.uadeapps.tabley.data.repository


import com.uadeapps.tabley.data.local.dao.ColumnDao
import com.uadeapps.tabley.data.local.entity.ColumnEntity
import com.uadeapps.tabley.domain.model.TableColumn
import com.uadeapps.tabley.domain.repository.ColumnRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.uadeapps.tabley.domain.model.CellType

class ColumnRepositoryImpl @Inject constructor(
    private val columnDao: ColumnDao
) : ColumnRepository {

    override fun getColumnsByTable(tableId: Long): Flow<List<TableColumn>> {
        return columnDao.getColumnsByTable(tableId).map { columns ->
            columns.map { it.toDomain() }
        }
    }

    override suspend fun getColumnById(id: Long): TableColumn? =
        columnDao.getColumnById(id)?.toDomain()

    override suspend fun insertColumn(column: TableColumn): Long =
        columnDao.insertColumn(column.toEntity())

    override suspend fun updateColumn(column: TableColumn) =
        columnDao.updateColumn(column.toEntity())

    override suspend fun deleteColumn(column: TableColumn) =
        columnDao.deleteColumn(column.toEntity())

    private fun ColumnEntity.toDomain() = TableColumn(
        id = id,
        tableId = tableId,
        name = name,
        type = CellType.valueOf(type),
        position = position
    )

    private fun TableColumn.toEntity() = ColumnEntity(
        id = id,
        tableId = tableId,
        name = name,
        type = type.name,
        position = position
    )
}
