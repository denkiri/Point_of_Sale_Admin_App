package com.denkiri.pharmacy.storage.daos
import androidx.lifecycle.LiveData
import androidx.room.*
import com.denkiri.pharmacy.models.dashboard.LastMonthProfit
@Dao
interface LastMonthProfitDao {
    @Query("SELECT *FROM LastMonthProfit LIMIT 1")
    fun getLastMonthProfit(): LiveData<LastMonthProfit>
    @Query("SELECT * FROM LastMonthProfit LIMIT 1")
    fun fetch(): LastMonthProfit
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLastMonthProfit(model: LastMonthProfit)
    @Delete
    fun deleteLastMonthProfit(model: LastMonthProfit)
    @Query("DELETE FROM LastMonthProfit")
    fun delete()
}