package com.uadeapps.tabley.domain.usecase.cell


import com.uadeapps.tabley.domain.model.Cell
import com.uadeapps.tabley.domain.repository.CellRepository
import javax.inject.Inject

class DeleteCellUseCase @Inject constructor(
    private val repository: CellRepository
) {
    suspend operator fun invoke(cell: Cell) {
        repository.deleteCell(cell)
    }
}
