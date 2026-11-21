package com.uadeapps.tabley.data.local.dao



import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.uadeapps.tabley.data.local.entity.ColumnEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ColumnDao {

    @Query("SELECT * FROM `columns` WHERE tableId = :tableId ORDER BY position ASC")
    fun getColumnsByTable(tableId: Long): Flow<List<ColumnEntity>>

    @Query("SELECT * FROM `columns` WHERE id = :id")
    suspend fun getColumnById(id: Long): ColumnEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertColumn(column: ColumnEntity): Long

    @Update
    suspend fun updateColumn(column: ColumnEntity)

    @Delete
    suspend fun deleteColumn(column: ColumnEntity)
}
