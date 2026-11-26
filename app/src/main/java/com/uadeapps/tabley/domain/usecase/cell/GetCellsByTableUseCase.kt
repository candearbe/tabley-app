package com.uadeapps.tabley.domain.usecase.cell

import com.uadeapps.tabley.domain.model.Cell
import com.uadeapps.tabley.domain.repository.CellRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCellsByTableUseCase @Inject constructor(
    private val repository: CellRepository
) {
    operator fun invoke(tableId: Long): Flow<List<Cell>> =
        repository.getCellsByTable(tableId)
}
