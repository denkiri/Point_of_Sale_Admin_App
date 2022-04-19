package com.denkiri.pharmacy.storage.daos
import androidx.lifecycle.LiveData
import androidx.room.*
import com.denkiri.pharmacy.models.expense.NetProfit
@Dao
interface NetProfitDao {
    @Query("SELECT *FROM NetProfit LIMIT 1")
    fun getTotalProfit(): LiveData<NetProfit>
    @Query("SELECT * FROM NetProfit LIMIT 1")
    fun fetch(): NetProfit
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTotalProfit(model: NetProfit)
    @Delete
    fun deleteTotalProfit(model: NetProfit)
    @Query("DELETE FROM NetProfit")
    fun delete()
}