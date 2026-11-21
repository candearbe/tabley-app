package com.uadeapps.tabley.domain.usecase.column


import com.uadeapps.tabley.domain.model.TableColumn
import com.uadeapps.tabley.domain.repository.ColumnRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetColumnsByTableUseCase @Inject constructor(
    private val repository: ColumnRepository
) {
    operator fun invoke(tableId: Long): Flow<List<TableColumn>> {
        return repository.getColumnsByTable(tableId)
    }
}
