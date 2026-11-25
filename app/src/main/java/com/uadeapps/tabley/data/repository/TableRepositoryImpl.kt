package com.uadeapps.tabley.data.repository



import com.uadeapps.tabley.data.local.dao.TableDao
import com.uadeapps.tabley.data.local.entity.TableEntity
import com.uadeapps.tabley.domain.model.TableSheet
import com.uadeapps.tabley.domain.repository.TableRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TableRepositoryImpl @Inject constructor(
    private val tableDao: TableDao
) : TableRepository {

    override fun getTablesByNotebook(notebookId: Long): Flow<List<TableSheet>> {
        return tableDao.getTablesByNotebook(notebookId).map { tables ->
            tables.map { it.toDomain() }
        }
    }

    override suspend fun getTableById(id: Long): TableSheet? {
        return tableDao.getTableById(id)?.toDomain()
    }

    override suspend fun insertTable(table: TableSheet): Long {
        return tableDao.insertTable(table.toEntity())
    }

    override suspend fun updateTable(table: TableSheet) {
        tableDao.updateTable(table.toEntity())
    }

    override suspend fun deleteTable(table: TableSheet) {
        tableDao.deleteTable(table.toEntity())
    }

    private fun TableEntity.toDomain(): TableSheet {
        return TableSheet(
            id = id,
            notebookId = notebookId,
            name = name
        )
    }

    private fun TableSheet.toEntity(): TableEntity {
        return TableEntity(
            id = id,
            notebookId = notebookId,
            name = name
        )
    }
}