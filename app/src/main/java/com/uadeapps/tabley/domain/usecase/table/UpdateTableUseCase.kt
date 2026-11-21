package com.uadeapps.tabley.domain.usecase.table


import com.uadeapps.tabley.domain.model.TableSheet
import com.uadeapps.tabley.domain.repository.TableRepository
import javax.inject.Inject

class UpdateTableUseCase @Inject constructor(
    private val repository: TableRepository
) {
    suspend operator fun invoke(table: TableSheet) {
        repository.updateTable(table)
    }
}
