package com.uadeapps.tabley.presentation.table_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.uadeapps.tabley.domain.model.Cell
import com.uadeapps.tabley.domain.model.CellType
import com.uadeapps.tabley.domain.model.TableColumn
import com.uadeapps.tabley.domain.model.TableRow

private val RowHeaderWidth = 64.dp
private val CellWidth = 160.dp

@Composable
fun TableDetailScreen(
    tableId: Long,
    tableName: String,
    onBackClick: () -> Unit,
    viewModel: TableDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    var showColumnDialog by remember { mutableStateOf(false) }
    var editingCell by remember { mutableStateOf<CellEditorData?>(null) }
    var columnToDelete by remember { mutableStateOf<TableColumn?>(null) }
    var rowToDelete by remember { mutableStateOf<TableRow?>(null) }

    LaunchedEffect(tableId) {
        viewModel.loadTable(tableId)
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearError()
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onBackClick) { Text("Volver") }
                Text(
                    text = tableName,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { showColumnDialog = true },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Nueva columna")
                }
                OutlinedButton(
                    onClick = viewModel::addRow,
                    enabled = uiState.columns.isNotEmpty(),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Nueva fila")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when {
                uiState.isLoading -> LoadingState()
                uiState.columns.isEmpty() -> EmptyColumnsState(
                    onAddColumn = { showColumnDialog = true }
                )
                uiState.rows.isEmpty() -> EmptyRowsState(onAddRow = viewModel::addRow)
                else -> Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    TableGrid(
                        uiState = uiState,
                        onCellClick = { row, column, cell ->
                            if (column.type == CellType.CHECKBOX) {
                                val nextValue =
                                    (cell?.value?.toBooleanStrictOrNull() != true).toString()
                                viewModel.saveCell(row.id, column.id, nextValue)
                            } else {
                                editingCell = CellEditorData(
                                    row,
                                    column,
                                    cell?.value.orEmpty()
                                )
                            }
                        },
                        onDeleteColumn = { columnToDelete = it },
                        onDeleteRow = { rowToDelete = it }
                    )
                }
            }
        }
    }

    if (showColumnDialog) {
        AddColumnDialog(
            onDismiss = { showColumnDialog = false },
            onConfirm = { name, type ->
                viewModel.addColumn(name, type)
                showColumnDialog = false
            }
        )
    }

    editingCell?.let { data ->
        CellEditorDialog(
            data = data,
            onDismiss = { editingCell = null },
            onConfirm = { value ->
                viewModel.saveCell(data.row.id, data.column.id, value)
                editingCell = null
            }
        )
    }

    columnToDelete?.let { column ->
        DeleteConfirmationDialog(
            title = "Eliminar columna",
            message = "Se eliminará la columna \"${column.name}\" y sus celdas.",
            onDismiss = { columnToDelete = null },
            onConfirm = {
                viewModel.deleteColumn(column)
                columnToDelete = null
            }
        )
    }

    rowToDelete?.let { row ->
        DeleteConfirmationDialog(
            title = "Eliminar fila",
            message = "Se eliminará esta fila y todos sus valores.",
            onDismiss = { rowToDelete = null },
            onConfirm = {
                viewModel.deleteRow(row)
                rowToDelete = null
            }
        )
    }
}

@Composable
private fun LoadingState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun EmptyColumnsState(onAddColumn: () -> Unit) {
    EmptyStateCard(
        title = "La tabla todavía no tiene columnas",
        description = "Creá la primera columna y elegí qué tipo de dato va a contener.",
        buttonText = "Crear columna",
        onClick = onAddColumn
    )
}

@Composable
private fun EmptyRowsState(onAddRow: () -> Unit) {
    EmptyStateCard(
        title = "La tabla todavía no tiene filas",
        description = "Agregá una fila para empezar a cargar información.",
        buttonText = "Crear fila",
        onClick = onAddRow
    )
}

@Composable
private fun EmptyStateCard(
    title: String,
    description: String,
    buttonText: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onClick) { Text(buttonText) }
        }
    }
}

@Composable
private fun TableGrid(
    uiState: TableDetailUiState,
    onCellClick: (TableRow, TableColumn, Cell?) -> Unit,
    onDeleteColumn: (TableColumn) -> Unit,
    onDeleteRow: (TableRow) -> Unit
) {
    val horizontalScrollState = rememberScrollState()
    val gridWidth = RowHeaderWidth + CellWidth * uiState.columns.size.toFloat()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize()
            .horizontalScroll(horizontalScrollState)
    ) {
        LazyColumn(modifier = Modifier.width(gridWidth)) {
            item {
                Row {
                    HeaderCell("#", RowHeaderWidth)
                    uiState.columns.forEach { column ->
                        ColumnHeaderCell(
                            column = column,
                            onDelete = { onDeleteColumn(column) }
                        )
                    }
                }
            }

            items(uiState.rows, key = { it.id }) { row ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RowHeaderCell(
                        position = row.position + 1,
                        onDelete = { onDeleteRow(row) }
                    )
                    uiState.columns.forEach { column ->
                        val cell = uiState.cells[row.id to column.id]
                        DataCell(
                            column = column,
                            value = cell?.value.orEmpty(),
                            onClick = { onCellClick(row, column, cell) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HeaderCell(text: String, width: Dp) {
    Box(
        modifier = Modifier
            .width(width)
            .height(68.dp)
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Text(text, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun ColumnHeaderCell(column: TableColumn, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .width(CellWidth)
            .height(68.dp)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(start = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(column.name, fontWeight = FontWeight.SemiBold, maxLines = 1)
            Text(
                text = column.type.displayName,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        TextButton(onClick = onDelete) { Text("×") }
    }
}

@Composable
private fun RowHeaderCell(position: Int, onDelete: () -> Unit) {
    Column(
        modifier = Modifier
            .width(RowHeaderWidth)
            .height(64.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(position.toString(), fontWeight = FontWeight.Medium)
        Text(
            text = "×",
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier
                .clickable(onClick = onDelete)
                .padding(horizontal = 12.dp)
        )
    }
}

@Composable
private fun DataCell(column: TableColumn, value: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(CellWidth)
            .height(64.dp)
            .padding(2.dp)
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(6.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        val shownValue = when {
            column.type == CellType.CHECKBOX && value.toBooleanStrictOrNull() == true -> "✓ Sí"
            column.type == CellType.CHECKBOX -> "☐ No"
            value.isBlank() -> "Tocar para editar"
            else -> value
        }
        Text(
            text = shownValue,
            color = if (value.isBlank() && column.type != CellType.CHECKBOX) {
                MaterialTheme.colorScheme.onSurfaceVariant
            } else {
                MaterialTheme.colorScheme.onSurface
            },
            maxLines = 2
        )
    }
}

@Composable
private fun AddColumnDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, CellType) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(CellType.TEXT) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva columna") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") },
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text("Tipo de dato", fontWeight = FontWeight.Medium)
                CellType.entries.forEach { type ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedType = type },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedType == type,
                            onClick = { selectedType = type }
                        )
                        Text(type.displayName)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(name.trim(), selectedType) },
                enabled = name.isNotBlank()
            ) {
                Text("Crear")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@Composable
private fun CellEditorDialog(
    data: CellEditorData,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var value by remember(data) { mutableStateOf(data.value) }
    val keyboardType = if (data.column.type == CellType.NUMBER) {
        KeyboardType.Decimal
    } else {
        KeyboardType.Text
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(data.column.name) },
        text = {
            OutlinedTextField(
                value = value,
                onValueChange = { newValue ->
                    if (data.column.type != CellType.NUMBER || newValue.isValidDecimalInput()) {
                        value = newValue
                    }
                },
                label = { Text(data.column.type.displayName) },
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                singleLine = data.column.type != CellType.LIST
            )
        },
        confirmButton = {
            Button(onClick = { onConfirm(value.trim()) }) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@Composable
private fun DeleteConfirmationDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            Button(onClick = onConfirm) { Text("Eliminar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

private data class CellEditorData(
    val row: TableRow,
    val column: TableColumn,
    val value: String
)

private val CellType.displayName: String
    get() = when (this) {
        CellType.TEXT -> "Texto"
        CellType.NUMBER -> "Número"
        CellType.DATE -> "Fecha"
        CellType.CHECKBOX -> "Sí / No"
        CellType.LIST -> "Lista o notas"
    }

private fun String.isValidDecimalInput(): Boolean =
    isBlank() || replace(',', '.').toDoubleOrNull() != null || this == "-"
