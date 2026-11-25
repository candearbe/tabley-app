package com.uadeapps.tabley.domain.usecase.cell


import com.uadeapps.tabley.domain.model.Cell
import com.uadeapps.tabley.domain.repository.CellRepository
import javax.inject.Inject

class SaveCellUseCase @Inject constructor(
    private val repository: CellRepository
) {
    suspend operator fun invoke(cell: Cell) {
        val existingCell = repository.getCell(
            rowId = cell.rowId,
            columnId = cell.columnId
        )

        if (existingCell != null) {
            repository.updateCell(
                cell.copy(id = existingCell.id)
            )
        } else {
            repository.insertCell(cell)
        }
    }
}
