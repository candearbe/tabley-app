package com.uadeapps.tabley.data.local.entity


import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "columns",
    foreignKeys = [
        ForeignKey(
            entity = TableEntity::class,
            parentColumns = ["id"],
            childColumns = ["tableId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("tableId")]
)
data class ColumnEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val tableId: Long,

    val name: String,

    val type: String,

    val position: Int
)
