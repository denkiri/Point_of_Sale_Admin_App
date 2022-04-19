package com.denkiri.pharmacy.storage.daos
import androidx.lifecycle.LiveData
import androidx.room.*
import com.denkiri.pharmacy.models.expense.MonthlyNetProfit
@Dao
interface MonthlyNetProfitDao {
    @Query("SELECT *FROM MonthlyNetProfit LIMIT 1")
    fun getMonthlyNetProfit(): LiveData<MonthlyNetProfit>
    @Query("SELECT * FROM MonthlyNetProfit LIMIT 1")
    fun fetch(): MonthlyNetProfit
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMonthlyNetProfit(model: MonthlyNetProfit)
    @Delete
    fun deleteMonthlyNetProfit(model: MonthlyNetProfit)
    @Query("DELETE FROM MonthlyNetProfit")
    fun delete()
}