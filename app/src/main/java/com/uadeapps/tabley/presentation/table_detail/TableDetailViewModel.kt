package com.uadeapps.tabley.presentation.table_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uadeapps.tabley.domain.model.Cell
import com.uadeapps.tabley.domain.model.CellType
import com.uadeapps.tabley.domain.model.TableColumn
import com.uadeapps.tabley.domain.model.TableRow
import com.uadeapps.tabley.domain.usecase.cell.GetCellsByTableUseCase
import com.uadeapps.tabley.domain.usecase.cell.SaveCellUseCase
import com.uadeapps.tabley.domain.usecase.column.CreateColumnUseCase
import com.uadeapps.tabley.domain.usecase.column.DeleteColumnUseCase
import com.uadeapps.tabley.domain.usecase.column.GetColumnsByTableUseCase
import com.uadeapps.tabley.domain.usecase.row.CreateRowUseCase
import com.uadeapps.tabley.domain.usecase.row.DeleteRowUseCase
import com.uadeapps.tabley.domain.usecase.row.GetRowsByTableUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TableDetailUiState(
    val isLoading: Boolean = true,
    val columns: List<TableColumn> = emptyList(),
    val rows: List<TableRow> = emptyList(),
    val cells: Map<Pair<Long, Long>, Cell> = emptyMap(),
    val errorMessage: String? = null
)

@HiltViewModel
class TableDetailViewModel @Inject constructor(
    private val getColumnsByTableUseCase: GetColumnsByTableUseCase,
    private val getRowsByTableUseCase: GetRowsByTableUseCase,
    private val getCellsByTableUseCase: GetCellsByTableUseCase,
    private val createColumnUseCase: CreateColumnUseCase,
    private val createRowUseCase: CreateRowUseCase,
    private val saveCellUseCase: SaveCellUseCase,
    private val deleteColumnUseCase: DeleteColumnUseCase,
    private val deleteRowUseCase: DeleteRowUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TableDetailUiState())
    val uiState: StateFlow<TableDetailUiState> = _uiState.asStateFlow()

    private var currentTableId: Long? = null
    private var observationJob: Job? = null

    fun loadTable(tableId: Long) {
        if (currentTableId == tableId && observationJob?.isActive == true) return

        currentTableId = tableId
        observationJob?.cancel()
        _uiState.value = TableDetailUiState(isLoading = true)

        observationJob = viewModelScope.launch {
            combine(
                getColumnsByTableUseCase(tableId),
                getRowsByTableUseCase(tableId),
                getCellsByTableUseCase(tableId)
            ) { columns, rows, cells ->
                TableDetailUiState(
                    isLoading = false,
                    columns = columns,
                    rows = rows,
                    cells = cells.associateBy { it.rowId to it.columnId }
                )
            }
                .catch { throwable ->
                    _uiState.value = TableDetailUiState(
                        isLoading = false,
                        errorMessage = throwable.message
                            ?: "No se pudo cargar la tabla"
                    )
                }
                .collect { state -> _uiState.value = state }
        }
    }

    fun addColumn(name: String, type: CellType) = runOperation {
        val tableId = requireNotNull(currentTableId)
        val nextPosition = (_uiState.value.columns.maxOfOrNull { it.position } ?: -1) + 1
        createColumnUseCase(tableId, name.trim(), type, nextPosition)
    }

    fun addRow() = runOperation {
        val tableId = requireNotNull(currentTableId)
        val nextPosition = (_uiState.value.rows.maxOfOrNull { it.position } ?: -1) + 1
        createRowUseCase(tableId, nextPosition)
    }

    fun saveCell(rowId: Long, columnId: Long, value: String) = runOperation {
        val existing = _uiState.value.cells[rowId to columnId]
        saveCellUseCase(
            Cell(
                id = existing?.id ?: 0L,
                rowId = rowId,
                columnId = columnId,
                value = value,
                backgroundColor = existing?.backgroundColor
            )
        )
    }

    fun deleteColumn(column: TableColumn) = runOperation {
        deleteColumnUseCase(column)
    }

    fun deleteRow(row: TableRow) = runOperation {
        deleteRowUseCase(row)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    private fun runOperation(operation: suspend () -> Unit) {
        viewModelScope.launch {
            runCatching { operation() }
                .onFailure { throwable ->
                    _uiState.value = _uiState.value.copy(
                        errorMessage = throwable.message
                            ?: "No se pudo completar la operación"
                    )
                }
        }
    }
}
