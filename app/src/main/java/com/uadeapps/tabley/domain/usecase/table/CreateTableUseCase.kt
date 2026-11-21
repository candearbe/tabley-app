package com.uadeapps.tabley.domain.usecase.table


import com.uadeapps.tabley.domain.model.TableSheet
import com.uadeapps.tabley.domain.repository.TableRepository
import javax.inject.Inject

class CreateTableUseCase @Inject constructor(
    private val repository: TableRepository
) {
    suspend operator fun invoke(
        notebookId: Long,
        name: String
    ): Long {
        val table = TableSheet(
            notebookId = notebookId,
            name = name
        )

        return repository.insertTable(table)
    }
}
