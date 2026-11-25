package com.uadeapps.tabley.domain.usecase.column


import com.uadeapps.tabley.domain.model.CellType
import com.uadeapps.tabley.domain.model.TableColumn
import com.uadeapps.tabley.domain.repository.ColumnRepository
import javax.inject.Inject

class CreateColumnUseCase @Inject constructor(
    private val repository: ColumnRepository
) {
    suspend operator fun invoke(
        tableId: Long,
        name: String,
        type: CellType,
        position: Int
    ): Long {
        val column = TableColumn(
            tableId = tableId,
            name = name,
            type = type,
            position = position
        )

        return repository.insertColumn(column)
    }
}
