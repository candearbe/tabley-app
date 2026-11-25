package com.uadeapps.tabley.data.local.dao



import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.uadeapps.tabley.data.local.entity.CellEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CellDao {

    @Query("SELECT * FROM cells WHERE rowId = :rowId")
    fun getCellsByRow(rowId: Long): Flow<List<CellEntity>>

    @Query("SELECT * FROM cells WHERE columnId = :columnId")
    fun getCellsByColumn(columnId: Long): Flow<List<CellEntity>>

    @Query("SELECT * FROM cells WHERE rowId = :rowId AND columnId = :columnId")
    suspend fun getCell(rowId: Long, columnId: Long): CellEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCell(cell: CellEntity): Long

    @Update
    suspend fun updateCell(cell: CellEntity)

    @Delete
    suspend fun deleteCell(cell: CellEntity)

    @Query("DELETE FROM cells WHERE rowId = :rowId")
    suspend fun deleteCellsByRow(rowId: Long)

    @Query("DELETE FROM cells WHERE columnId = :columnId")
    suspend fun deleteCellsByColumn(columnId: Long)
}
