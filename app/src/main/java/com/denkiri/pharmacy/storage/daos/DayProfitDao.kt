package com.denkiri.pharmacy.storage.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.denkiri.pharmacy.models.dashboard.DayCost
import com.denkiri.pharmacy.models.dashboard.DayProfit
@Dao
interface DayProfitDao {
    @Query("SELECT *FROM DayProfit LIMIT 1")
    fun getDayProfit(): LiveData<DayProfit>
    @Query("SELECT * FROM DayProfit LIMIT 1")
    fun fetch(): DayProfit
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDayProfit(model: DayProfit)
    @Delete
    fun deleteDayProfit(model: DayProfit)
    @Query("DELETE FROM DayProfit")
    fun delete()
}