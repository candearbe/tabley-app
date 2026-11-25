package com.uadeapps.tabley.domain.model

data class Cell(
    val id: Long = 0L,
    val rowId: Long,
    val columnId: Long,
    val value: String,
    val backgroundColor: String? = null
)