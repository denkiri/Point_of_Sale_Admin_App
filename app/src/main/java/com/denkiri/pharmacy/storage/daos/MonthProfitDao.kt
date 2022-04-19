package com.denkiri.pharmacy.storage.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.denkiri.pharmacy.models.dashboard.MonthProfit
@Dao
interface MonthProfitDao {
    @Query("SELECT *FROM MonthProfit LIMIT 1")
    fun getMonthProfit(): LiveData<MonthProfit>
    @Query("SELECT * FROM MonthProfit LIMIT 1")
    fun fetch(): MonthProfit
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMonthProfit(model: MonthProfit)
    @Delete
    fun deleteMonthProfit(model: MonthProfit)
    @Query("DELETE FROM MonthProfit")
    fun delete()
}