package com.denkiri.pharmacy.storage.daos
import androidx.lifecycle.LiveData
import androidx.room.*
import com.denkiri.pharmacy.models.reports.incomeReport.MonthlyCashierIncome
@Dao
interface MonthlyCashierIncomeDao {
    @Query("SELECT *FROM MonthlyCashierIncome LIMIT 1")
    fun getMonthlyCashierIncome(): LiveData<MonthlyCashierIncome>
    @Query("SELECT * FROM MonthlyCashierIncome LIMIT 1")
    fun fetch(): MonthlyCashierIncome
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMonthlyCashierIncome(model: MonthlyCashierIncome)
    @Delete
    fun deleteMonthlyCashierIncome(model: MonthlyCashierIncome)
    @Query("DELETE FROM MonthlyCashierIncome")
    fun delete()
}