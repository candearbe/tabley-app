package com.uadeapps.tabley.domain.usecase.notebook



import com.uadeapps.tabley.domain.model.Notebook
import com.uadeapps.tabley.domain.repository.NotebookRepository
import javax.inject.Inject

class DeleteNotebookUseCase @Inject constructor(
    private val repository: NotebookRepository
) {
    suspend operator fun invoke(notebook: Notebook) {
        repository.deleteNotebook(notebook)
    }
}
