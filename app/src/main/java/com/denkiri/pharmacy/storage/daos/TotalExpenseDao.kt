package com.denkiri.pharmacy.storage.daos
import androidx.lifecycle.LiveData
import androidx.room.*
import com.denkiri.pharmacy.models.expense.TotalExpense
@Dao
interface TotalExpenseDao {
    @Query("SELECT *FROM TotalExpense LIMIT 1")
    fun getTotalExpense(): LiveData<TotalExpense>
    @Query("SELECT * FROM TotalExpense LIMIT 1")
    fun fetch(): TotalExpense
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTotalExpense(model: TotalExpense)
    @Delete
    fun deleteTotalExpense(model: TotalExpense)
    @Query("DELETE FROM TotalExpense")
    fun delete()
}