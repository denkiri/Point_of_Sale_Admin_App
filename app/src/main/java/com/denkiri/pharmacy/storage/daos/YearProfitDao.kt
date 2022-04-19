package com.denkiri.pharmacy.storage.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.denkiri.pharmacy.models.dashboard.YearProfit
@Dao
interface YearProfitDao {
    @Query("SELECT *FROM YearProfit  LIMIT 1")
    fun getYearProfit(): LiveData<YearProfit>
    @Query("SELECT * FROM YearProfit LIMIT 1")
    fun fetch(): YearProfit
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertYearProfit(model: YearProfit)
    @Delete
    fun deleteYearProfit(model: YearProfit)
    @Query("DELETE FROM YearProfit")
    fun delete()
}