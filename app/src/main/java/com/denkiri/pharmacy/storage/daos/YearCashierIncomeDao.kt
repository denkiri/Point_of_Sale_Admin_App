package com.denkiri.pharmacy.storage.daos
import androidx.lifecycle.LiveData
import androidx.room.*
import com.denkiri.pharmacy.models.reports.incomeReport.YearCashierIncome
@Dao
interface YearCashierIncomeDao {
    @Query("SELECT *FROM YearCashierIncome LIMIT 1")
    fun getYearCashierIncome(): LiveData<YearCashierIncome>
    @Query("SELECT * FROM YearCashierIncome LIMIT 1")
    fun fetch():YearCashierIncome
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertYearCashierIncome(model: YearCashierIncome)
    @Delete
    fun deleteYearCashierIncome(model: YearCashierIncome)
    @Query("DELETE FROM YearCashierIncome")
    fun delete()
}