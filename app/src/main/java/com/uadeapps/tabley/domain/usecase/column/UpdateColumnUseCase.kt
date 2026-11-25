package com.uadeapps.tabley.domain.usecase.column

import com.uadeapps.tabley.domain.model.TableColumn
import com.uadeapps.tabley.domain.repository.ColumnRepository
import javax.inject.Inject

class UpdateColumnUseCase @Inject constructor(
    private val repository: ColumnRepository
) {
    suspend operator fun invoke(column: TableColumn) {
        repository.updateColumn(column)
    }
}
