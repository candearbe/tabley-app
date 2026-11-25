package com.uadeapps.tabley.domain.usecase.notebook


import com.uadeapps.tabley.domain.model.Notebook
import com.uadeapps.tabley.domain.repository.NotebookRepository
import javax.inject.Inject

class CreateNotebookUseCase @Inject constructor(
    private val repository: NotebookRepository
) {
    suspend operator fun invoke(
        name: String,
        color: String
    ): Long {
        val notebook = Notebook(
            name = name,
            color = color
        )

        return repository.insertNotebook(notebook)
    }
}
