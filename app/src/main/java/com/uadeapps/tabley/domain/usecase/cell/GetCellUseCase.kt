package com.uadeapps.tabley.domain.usecase.cell


import com.uadeapps.tabley.domain.repository.CellRepository
import javax.inject.Inject

class GetCellUseCase @Inject constructor(
    private val repository: CellRepository
) {
    suspend operator fun invoke(
        rowId: Long,
        columnId: Long
    ) = repository.getCell(rowId, columnId)
}
