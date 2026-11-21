package com.uadeapps.tabley.data.local


import androidx.room.Database
import androidx.room.RoomDatabase
import com.uadeapps.tabley.data.local.dao.CellDao
import com.uadeapps.tabley.data.local.dao.ColumnDao
import com.uadeapps.tabley.data.local.dao.NotebookDao
import com.uadeapps.tabley.data.local.dao.RowDao
import com.uadeapps.tabley.data.local.dao.TableDao
import com.uadeapps.tabley.data.local.entity.CellEntity
import com.uadeapps.tabley.data.local.entity.ColumnEntity
import com.uadeapps.tabley.data.local.entity.NotebookEntity
import com.uadeapps.tabley.data.local.entity.RowEntity
import com.uadeapps.tabley.data.local.entity.TableEntity

@Database(
    entities = [
        NotebookEntity::class,
        TableEntity::class,
        ColumnEntity::class,
        RowEntity::class,
        CellEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class TableyDatabase : RoomDatabase() {

    abstract fun notebookDao(): NotebookDao

    abstract fun tableDao(): TableDao

    abstract fun columnDao(): ColumnDao

    abstract fun rowDao(): RowDao

    abstract fun cellDao(): CellDao
}
