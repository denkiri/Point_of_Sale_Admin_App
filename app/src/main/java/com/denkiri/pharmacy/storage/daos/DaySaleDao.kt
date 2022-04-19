package com.denkiri.pharmacy.storage.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.denkiri.pharmacy.models.dashboard.DaySale
@Dao
interface DaySaleDao {
    @Query("SELECT *FROM DaySale LIMIT 1")
    fun getDaySale(): LiveData<DaySale>
    @Query("SELECT * FROM DaySale LIMIT 1")
    fun fetch(): DaySale
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDaySale(model: DaySale)
    @Delete
    fun deleteDaySale(model: DaySale)
    @Query("DELETE FROM DaySale")
    fun delete()
}