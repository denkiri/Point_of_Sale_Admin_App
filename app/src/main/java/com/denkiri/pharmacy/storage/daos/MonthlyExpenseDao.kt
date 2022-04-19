package com.denkiri.pharmacy.storage.daos
import androidx.lifecycle.LiveData
import androidx.room.*
import com.denkiri.pharmacy.models.expense.MonthlyExpense
@Dao
interface MonthlyExpenseDao {
    @Query("SELECT *FROM MonthlyExpense LIMIT 1")
    fun getMonthlyExpense(): LiveData<MonthlyExpense>
    @Query("SELECT * FROM MonthlyExpense LIMIT 1")
    fun fetch(): MonthlyExpense
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMonthlyExpense(model: MonthlyExpense)
    @Delete
    fun deleteMonthlyExpense(model: MonthlyExpense)
    @Query("DELETE FROM MonthlyExpense")
    fun delete()
}