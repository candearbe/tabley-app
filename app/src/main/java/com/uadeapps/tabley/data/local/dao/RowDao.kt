package com.uadeapps.tabley.data.local.dao



import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.uadeapps.tabley.data.local.entity.RowEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RowDao {

    @Query("SELECT * FROM `rows` WHERE tableId = :tableId ORDER BY position ASC")
    fun getRowsByTable(tableId: Long): Flow<List<RowEntity>>

    @Query("SELECT * FROM `rows` WHERE id = :id")
    suspend fun getRowById(id: Long): RowEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRow(row: RowEntity): Long

    @Update
    suspend fun updateRow(row: RowEntity)

    @Delete
    suspend fun deleteRow(row: RowEntity)
}
