package com.uadeapps.tabley.domain.usecase.row

import com.uadeapps.tabley.domain.model.TableRow
import com.uadeapps.tabley.domain.repository.RowRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRowsByTableUseCase @Inject constructor(
    private val repository: RowRepository
) {
    operator fun invoke(tableId: Long): Flow<List<TableRow>> {
        return repository.getRowsByTable(tableId)
    }
}
