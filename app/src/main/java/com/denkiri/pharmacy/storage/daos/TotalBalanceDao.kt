package com.denkiri.pharmacy.storage.daos
import androidx.lifecycle.LiveData
import androidx.room.*
import com.denkiri.pharmacy.models.reports.accountReceivable.TotalBalance

@Dao
interface TotalBalanceDao {
    @Query("SELECT *FROM TotalBalance LIMIT 1")
    fun getTotalBalance(): LiveData<TotalBalance>
    @Query("SELECT * FROM TotalBalance LIMIT 1")
    fun fetch(): TotalBalance
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTotalBalance(model: TotalBalance)
    @Delete
    fun deleteTotalBalance(model: TotalBalance)
    @Query("DELETE FROM TotalBalance")
    fun delete()
}