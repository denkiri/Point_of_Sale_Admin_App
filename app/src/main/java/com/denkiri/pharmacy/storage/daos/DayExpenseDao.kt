package com.denkiri.pharmacy.storage.daos
import androidx.lifecycle.LiveData
import androidx.room.*
import com.denkiri.pharmacy.models.expense.DayExpense
@Dao
interface DayExpenseDao {
    @Query("SELECT *FROM DayExpense LIMIT 1")
    fun getDayExpense(): LiveData<DayExpense>
    @Query("SELECT * FROM DayExpense LIMIT 1")
    fun fetch(): DayExpense
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDayExpense(model: DayExpense)
    @Delete
    fun deleteDayExpense(model: DayExpense)
    @Query("DELETE FROM DayExpense")
    fun delete()
}