package com.uadeapps.tabley.domain.usecase.table


import com.uadeapps.tabley.domain.repository.TableRepository
import javax.inject.Inject

class GetTablesByNotebookUseCase @Inject constructor(
    private val repository: TableRepository
) {
    operator fun invoke(notebookId: Long) =
        repository.getTablesByNotebook(notebookId)
}
