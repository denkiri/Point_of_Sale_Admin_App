package com.denkiri.pharmacy.storage.daos
import androidx.lifecycle.LiveData
import androidx.room.*
import com.denkiri.pharmacy.models.expense.YearlyIncome
@Dao
interface YearlyIncomeDao {
    @Query("SELECT *FROM YearlyIncome LIMIT 1")
    fun getYearlyIncome(): LiveData<YearlyIncome>
    @Query("SELECT * FROM YearlyIncome LIMIT 1")
    fun fetch(): YearlyIncome
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertYearlyIncome(model: YearlyIncome)
    @Delete
    fun deleteYearlyIncome(model: YearlyIncome)
    @Query("DELETE FROM YearlyIncome")
    fun delete()
}