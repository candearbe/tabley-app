package com.uadeapps.tabley.domain.model

data class TableColumn(
    val id: Long = 0L,
    val tableId: Long,
    val name: String,
    val type: CellType,
    val position: Int
)