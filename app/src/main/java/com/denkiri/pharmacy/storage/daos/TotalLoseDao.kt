package com.denkiri.pharmacy.storage.daos
import androidx.lifecycle.LiveData
import androidx.room.*
import com.denkiri.pharmacy.models.reports.expiredproducts.TotalLose
@Dao
interface TotalLoseDao {
    @Query("SELECT *FROM TotalLose LIMIT 1")
    fun getTotalLose(): LiveData<TotalLose>
    @Query("SELECT * FROM TotalLose LIMIT 1")
    fun fetch(): TotalLose
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTotalLose(model: TotalLose)
    @Delete
    fun deleteTotalLose(model: TotalLose)
    @Query("DELETE FROM TotalLose")
    fun delete()
}