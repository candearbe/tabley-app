package com.uadeapps.tabley.domain.usecase.cell


import com.uadeapps.tabley.domain.repository.CellRepository
import javax.inject.Inject

class GetCellsByColumnUseCase @Inject constructor(
    private val repository: CellRepository
) {
    operator fun invoke(columnId: Long) =
        repository.getCellsByColumn(columnId)
}
