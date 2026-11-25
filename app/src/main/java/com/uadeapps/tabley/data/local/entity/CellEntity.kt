package com.uadeapps.tabley.data.local.entity


import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "cells",
    foreignKeys = [
        ForeignKey(
            entity = RowEntity::class,
            parentColumns = ["id"],
            childColumns = ["rowId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ColumnEntity::class,
            parentColumns = ["id"],
            childColumns = ["columnId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("rowId"),
        Index("columnId")
    ]
)
data class CellEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val rowId: Long,

    val columnId: Long,

    val value: String = "",

    val color: String? = null
)
