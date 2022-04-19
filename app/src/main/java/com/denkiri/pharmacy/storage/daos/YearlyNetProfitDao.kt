package com.denkiri.pharmacy.storage.daos
import androidx.lifecycle.LiveData
import androidx.room.*
import com.denkiri.pharmacy.models.expense.YearlyNetProfit
@Dao
interface YearlyNetProfitDao {
    @Query("SELECT *FROM YearlyNetProfit LIMIT 1")
    fun getYearlyNetProfit(): LiveData<YearlyNetProfit>
    @Query("SELECT * FROM YearlyNetProfit LIMIT 1")
    fun fetch(): YearlyNetProfit
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertYearlyNetProfit(model: YearlyNetProfit)
    @Delete
    fun deleteYearlyNetProfit(model: YearlyNetProfit)
    @Query("DELETE FROM YearlyNetProfit")
    fun delete()
}