package com.uadeapps.tabley.domain.usecase.row


import com.uadeapps.tabley.domain.model.TableRow
import com.uadeapps.tabley.domain.repository.RowRepository
import javax.inject.Inject

class CreateRowUseCase @Inject constructor(
    private val repository: RowRepository
) {
    suspend operator fun invoke(
        tableId: Long,
        position: Int
    ): Long {
        val row = TableRow(
            tableId = tableId,
            position = position
        )

        return repository.insertRow(row)
    }
}
