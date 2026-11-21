package com.uadeapps.tabley.data.repository


import com.uadeapps.tabley.data.local.dao.NotebookDao
import com.uadeapps.tabley.data.local.entity.NotebookEntity
import com.uadeapps.tabley.domain.model.Notebook
import com.uadeapps.tabley.domain.repository.NotebookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NotebookRepositoryImpl @Inject constructor(
    private val notebookDao: NotebookDao
) : NotebookRepository {

    override fun getAllNotebooks(): Flow<List<Notebook>> {
        return notebookDao.getAllNotebooks().map { notebooks ->
            notebooks.map { it.toDomain() }
        }
    }

    override suspend fun getNotebookById(id: Long): Notebook? {
        return notebookDao.getNotebookById(id)?.toDomain()
    }

    override suspend fun insertNotebook(notebook: Notebook): Long {
        return notebookDao.insertNotebook(notebook.toEntity())
    }

    override suspend fun updateNotebook(notebook: Notebook) {
        notebookDao.updateNotebook(notebook.toEntity())
    }

    override suspend fun deleteNotebook(notebook: Notebook) {
        notebookDao.deleteNotebook(notebook.toEntity())
    }

    private fun NotebookEntity.toDomain(): Notebook {
        return Notebook(
            id = id,
            name = name,
            color = color
        )
    }

    private fun Notebook.toEntity(): NotebookEntity {
        return NotebookEntity(
            id = id,
            name = name,
            color = color
        )
    }
}
