package com.denkiri.pharmacy.storage.daos
import androidx.lifecycle.LiveData
import androidx.room.*
import com.denkiri.pharmacy.models.expense.MonthlyIncome
@Dao
interface MonthlyIncomeDao {
    @Query("SELECT *FROM MonthlyIncome LIMIT 1")
    fun getMonthlyIncome(): LiveData<MonthlyIncome>
    @Query("SELECT * FROM MonthlyIncome LIMIT 1")
    fun fetch(): MonthlyIncome
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMonthlyIncome(model: MonthlyIncome)
    @Delete
    fun deleteMonthlyIncome(model: MonthlyIncome)
    @Query("DELETE FROM MonthlyIncome")
    fun delete()
}