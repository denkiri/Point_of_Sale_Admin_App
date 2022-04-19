package com.denkiri.pharmacy.storage.daos
import androidx.lifecycle.LiveData
import androidx.room.*
import com.denkiri.pharmacy.models.reports.incomeReport.LastMonthCashierIncome
@Dao
interface LastMonthCashierIncomeDao {
    @Query("SELECT *FROM LastMonthCashierIncome LIMIT 1")
    fun getLastMonthCashierIncome(): LiveData<LastMonthCashierIncome>
    @Query("SELECT * FROM LastMonthCashierIncome LIMIT 1")
    fun fetch(): LastMonthCashierIncome
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLastMonthCashierIncome(model: LastMonthCashierIncome)
    @Delete
    fun deleteLastMonthCashierIncome(model: LastMonthCashierIncome)
    @Query("DELETE FROM LastMonthCashierIncome")
    fun delete()
}