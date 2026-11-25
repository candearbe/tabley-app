package com.uadeapps.tabley.presentation.notebooks


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.core.graphics.toColorInt
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import com.uadeapps.tabley.domain.model.Notebook
import androidx.compose.foundation.clickable
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun NotebooksScreen(
    onNotebookClick: (Long, String) -> Unit,
    viewModel: NotebookViewModel = hiltViewModel()
) {
    val notebooks by viewModel.notebooks.collectAsStateWithLifecycle()

    var showDialog by remember { mutableStateOf(false) }
    var notebookName by remember { mutableStateOf("") }

    var notebookColor by remember {
        mutableStateOf("#6750A4")
    }

    val notebookColors = listOf(
        "#6750A4",
        "#2196F3",
        "#4CAF50",
        "#FF9800",
        "#E91E63",
        "#795548"
    )

    var selectedNotebook by remember {
        mutableStateOf<Notebook?>(null)
    }

    var showOptions by remember {
        mutableStateOf(false)
    }

    var showDeleteDialog by remember {
        mutableStateOf(false)
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    selectedNotebook = null
                    notebookName = ""
                    notebookColor = "#6750A4"
                    showDialog = true
                }
            ) {
                Text("+")
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Mis cuadernos",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.size(16.dp))

            if (notebooks.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Todavía no tenés cuadernos")
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(notebooks) { notebook ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onNotebookClick(notebook.id, notebook.name)
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(
                                            color = try {
                                                Color(notebook.color.toColorInt())
                                            } catch (e: IllegalArgumentException) {
                                                MaterialTheme.colorScheme.primary
                                            },
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                )

                                Text(
                                    text = notebook.name,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 16.dp),
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Box {
                                    TextButton(
                                        onClick = {
                                            selectedNotebook = notebook
                                            showOptions = true
                                        }
                                    ) {
                                        Text("⋮")
                                    }

                                    DropdownMenu(
                                        expanded = showOptions &&
                                                selectedNotebook?.id == notebook.id,
                                        onDismissRequest = {
                                            showOptions = false
                                        }
                                    ) {
                                        DropdownMenuItem(
                                            text = { Text("Renombrar") },
                                            onClick = {
                                                showOptions = false
                                                notebookName = notebook.name
                                                notebookColor = notebook.color
                                                selectedNotebook = notebook
                                                showDialog = true
                                            }
                                        )

                                        DropdownMenuItem(
                                            text = { Text("Eliminar") },
                                            onClick = {
                                                showOptions = false
                                                selectedNotebook = notebook
                                                showDeleteDialog = true
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
                selectedNotebook = null
            },
            title = {
                Text(
                    if (selectedNotebook == null)
                        "Nuevo cuaderno"
                    else
                        "Renombrar cuaderno"
                )
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = notebookName,
                        onValueChange = { notebookName = it },
                        label = { Text("Nombre del cuaderno") },
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.size(16.dp))

                    Text("Elegí un color")

                    Spacer(modifier = Modifier.size(8.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        notebookColors.forEach { colorHex ->
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(
                                        color = Color(colorHex.toColorInt()),
                                        shape = RoundedCornerShape(50)
                                    )
                                    .clickable {
                                        notebookColor = colorHex
                                    }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (notebookName.isNotBlank()) {
                            val notebook = selectedNotebook

                            if (notebook == null) {
                                viewModel.createNotebook(
                                    name = notebookName.trim(),
                                    color = notebookColor
                                )
                            } else {
                                viewModel.updateNotebook(
                                    notebook.copy(
                                        name = notebookName.trim(),
                                        color = notebookColor
                                    )
                                )
                            }

                            showDialog = false
                            selectedNotebook = null
                        }
                    }
                ) {
                    Text(
                        if (selectedNotebook == null)
                            "Crear"
                        else
                            "Guardar"
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        selectedNotebook = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (showDeleteDialog && selectedNotebook != null) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
                selectedNotebook = null
            },
            title = {
                Text("Eliminar cuaderno")
            },
            text = {
                Text(
                    "¿Seguro que querés eliminar \"${selectedNotebook!!.name}\"?"
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        selectedNotebook?.let {
                            viewModel.deleteNotebook(it)
                        }

                        showDeleteDialog = false
                        selectedNotebook = null
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        selectedNotebook = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}
