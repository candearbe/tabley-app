package com.uadeapps.tabley.domain.usecase.cell


import com.uadeapps.tabley.domain.repository.CellRepository
import javax.inject.Inject

class GetCellsByRowUseCase @Inject constructor(
    private val repository: CellRepository
) {
    operator fun invoke(rowId: Long) =
        repository.getCellsByRow(rowId)
}
