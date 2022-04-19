package com.denkiri.pharmacy.storage.daos
import androidx.lifecycle.LiveData
import androidx.room.*
import com.denkiri.pharmacy.models.reports.incomeReport.DayCashierIncome
@Dao
interface DayCashierIncomeDao {
    @Query("SELECT *FROM DayCashierIncome LIMIT 1")
    fun getDayCashierIncome(): LiveData<DayCashierIncome>
    @Query("SELECT * FROM DayCashierIncome LIMIT 1")
    fun fetch(): DayCashierIncome
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDayCashierIncome(model: DayCashierIncome)
    @Delete
    fun deleteDayCashierIncome(model: DayCashierIncome)
    @Query("DELETE FROM DayCashierIncome")
    fun delete()
}