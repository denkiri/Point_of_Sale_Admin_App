package com.denkiri.pharmacy.storage.daos
import androidx.lifecycle.LiveData
import androidx.room.*
import com.denkiri.pharmacy.models.expense.DayNetProfit
@Dao
interface DayNetProfitDao {
    @Query("SELECT *FROM DayNetProfit LIMIT 1")
    fun getDayNetProfit(): LiveData<DayNetProfit>
    @Query("SELECT * FROM DayNetProfit LIMIT 1")
    fun fetch(): DayNetProfit
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDayNetProfit(model: DayNetProfit)
    @Delete
    fun deleteDayNetProfit(model:DayNetProfit)
    @Query("DELETE FROM DayNetProfit")
    fun delete()
}