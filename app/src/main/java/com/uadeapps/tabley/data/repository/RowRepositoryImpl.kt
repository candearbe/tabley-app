package com.uadeapps.tabley.data.repository

import com.uadeapps.tabley.data.local.dao.RowDao
import com.uadeapps.tabley.data.local.entity.RowEntity
import com.uadeapps.tabley.domain.model.TableRow
import com.uadeapps.tabley.domain.repository.RowRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RowRepositoryImpl @Inject constructor(
    private val rowDao: RowDao
) : RowRepository {

    override fun getRowsByTable(tableId: Long): Flow<List<TableRow>> {
        return rowDao.getRowsByTable(tableId).map { rows ->
            rows.map { it.toDomain() }
        }
    }

    override suspend fun getRowById(id: Long): TableRow? =
        rowDao.getRowById(id)?.toDomain()

    override suspend fun insertRow(row: TableRow): Long =
        rowDao.insertRow(row.toEntity())

    override suspend fun updateRow(row: TableRow) =
        rowDao.updateRow(row.toEntity())

    override suspend fun deleteRow(row: TableRow) =
        rowDao.deleteRow(row.toEntity())

    private fun RowEntity.toDomain() = TableRow(
        id = id,
        tableId = tableId,
        position = position
    )

    private fun TableRow.toEntity() = RowEntity(
        id = id,
        tableId = tableId,
        position = position
    )
}
