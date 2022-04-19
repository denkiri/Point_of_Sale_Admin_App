package com.denkiri.pharmacy.storage.daos
import androidx.lifecycle.LiveData
import androidx.room.*
import com.denkiri.pharmacy.models.expense.YearlyExpense
@Dao

interface YearlyExpenseDao {
    @Query("SELECT *FROM YearlyExpense LIMIT 1")
    fun getYearlyExpense(): LiveData<YearlyExpense>
    @Query("SELECT * FROM YearlyExpense LIMIT 1")
    fun fetch(): YearlyExpense
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertYearlyExpense(model: YearlyExpense)
    @Delete
    fun deleteYearlyExpense(model:YearlyExpense)
    @Query("DELETE FROM YearlyExpense")
    fun delete()
}