package com.uadeapps.tabley.presentation.notebook_detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.foundation.clickable
@Composable
fun NotebookDetailScreen(
    notebookId: Long,
    notebookName: String,
    onBackClick: () -> Unit,
    onTableClick: (Long, String) -> Unit,
    viewModel: NotebookDetailViewModel = hiltViewModel()
) {
    val tables by viewModel.getTables(notebookId).collectAsState()
    var showCreateDialog by remember { mutableStateOf(false) }
    var tableName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = onBackClick) {
            Text("Volver")
        }

        Text(
            text = notebookName,
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Tablas del cuaderno",
            style = MaterialTheme.typography.titleMedium
        )
        Button(
            onClick = {
                tableName = ""
                showCreateDialog = true
            }
        ) {
            Text("Nueva tabla")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (tables.isEmpty()) {
            Text("Todavía no hay tablas")
        } else {
            LazyColumn {
                items(tables) { table ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable {
                                onTableClick(table.id, table.name)
                            }
                    ) {
                        Text(
                            text = table.name,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }

        if (showCreateDialog) {
            AlertDialog(
                onDismissRequest = {
                    showCreateDialog = false
                },
                title = {
                    Text("Nueva tabla")
                },
                text = {
                    OutlinedTextField(
                        value = tableName,
                        onValueChange = { tableName = it },
                        label = { Text("Nombre de la tabla") },
                        singleLine = true
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (tableName.isNotBlank()) {
                                viewModel.createTable(
                                    notebookId,
                                    tableName.trim()
                                )
                                showCreateDialog = false
                            }
                        }
                    ) {
                        Text("Crear")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showCreateDialog = false
                        }
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}
