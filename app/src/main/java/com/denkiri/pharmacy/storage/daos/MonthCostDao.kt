package com.denkiri.pharmacy.storage.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.denkiri.pharmacy.models.dashboard.MonthCost
@Dao
interface MonthCostDao {
    @Query("SELECT *FROM MonthCost LIMIT 1")
    fun getMonthCost(): LiveData<MonthCost>
    @Query("SELECT * FROM MonthCost LIMIT 1")
    fun fetch(): MonthCost
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMonthCost(model: MonthCost)
    @Delete
    fun deleteMonthCost(model: MonthCost)
    @Query("DELETE FROM MonthCost")
    fun delete()
}