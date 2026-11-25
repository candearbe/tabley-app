package com.uadeapps.tabley.domain.usecase.row


import com.uadeapps.tabley.domain.model.TableRow
import com.uadeapps.tabley.domain.repository.RowRepository
import javax.inject.Inject

class DeleteRowUseCase @Inject constructor(
    private val repository: RowRepository
) {
    suspend operator fun invoke(row: TableRow) {
        repository.deleteRow(row)
    }
}
