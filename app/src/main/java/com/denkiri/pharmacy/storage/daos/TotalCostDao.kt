package com.denkiri.pharmacy.storage.daos
import androidx.lifecycle.LiveData
import androidx.room.*
import com.denkiri.pharmacy.models.reports.salesReport.TotalCost
@Dao
interface TotalCostDao {
    @Query("SELECT *FROM TotalCost LIMIT 1")
    fun getTotalCost(): LiveData<TotalCost>
    @Query("SELECT * FROM TotalCost LIMIT 1")
    fun fetch(): TotalCost
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTotalCost(model: TotalCost)
    @Delete
    fun deleteTotalCost(model: TotalCost)
    @Query("DELETE FROM TotalCost")
    fun delete()
}