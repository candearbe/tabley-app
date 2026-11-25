package com.uadeapps.tabley.domain.usecase.notebook


import com.uadeapps.tabley.domain.repository.NotebookRepository
import javax.inject.Inject

class GetAllNotebooksUseCase @Inject constructor(
    private val repository: NotebookRepository
) {
    operator fun invoke() = repository.getAllNotebooks()
}
