package com.uadeapps.tabley.domain.usecase.cell


import com.uadeapps.tabley.domain.repository.CellRepository
import javax.inject.Inject

class DeleteCellsByColumnUseCase @Inject constructor(
    private val repository: CellRepository
) {
    suspend operator fun invoke(columnId: Long) {
        repository.deleteCellsByColumn(columnId)
    }
}
