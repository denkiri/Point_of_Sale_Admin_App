package com.denkiri.pharmacy.storage.daos
import androidx.lifecycle.LiveData
import androidx.room.*
import com.denkiri.pharmacy.models.reports.salesReport.TotalSalesBalance
@Dao
interface TotalSalesBalanceDao {
    @Query("SELECT *FROM TotalSalesBalance LIMIT 1")
    fun getTotalBalance(): LiveData<TotalSalesBalance>
    @Query("SELECT * FROM TotalSalesBalance LIMIT 1")
    fun fetch(): TotalSalesBalance
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTotalBalance(model: TotalSalesBalance)
    @Delete
    fun deleteTotalBalance(model: TotalSalesBalance)
    @Query("DELETE FROM TotalSalesBalance")
    fun delete()
}