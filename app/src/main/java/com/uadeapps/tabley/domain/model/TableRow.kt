package com.uadeapps.tabley.domain.model

data class TableRow(
    val id: Long = 0L,
    val tableId: Long,
    val position: Int
)