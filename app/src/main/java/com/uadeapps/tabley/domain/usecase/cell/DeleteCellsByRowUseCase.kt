package com.uadeapps.tabley.domain.usecase.cell


import com.uadeapps.tabley.domain.repository.CellRepository
import javax.inject.Inject

class DeleteCellsByRowUseCase @Inject constructor(
    private val repository: CellRepository
) {
    suspend operator fun invoke(rowId: Long) {
        repository.deleteCellsByRow(rowId)
    }
}
