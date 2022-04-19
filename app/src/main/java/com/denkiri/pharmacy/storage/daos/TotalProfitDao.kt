package com.denkiri.pharmacy.storage.daos
import androidx.lifecycle.LiveData
import androidx.room.*
import com.denkiri.pharmacy.models.reports.salesReport.TotalProfit
@Dao
interface TotalProfitDao {
    @Query("SELECT *FROM TotalProfit LIMIT 1")
    fun getTotalProfit(): LiveData<TotalProfit>
    @Query("SELECT * FROM TotalProfit LIMIT 1")
    fun fetch(): TotalProfit
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTotalProfit(model: TotalProfit)
    @Delete
    fun deleteTotalProfit(model: TotalProfit)
    @Query("DELETE FROM TotalProfit")
    fun delete()
}