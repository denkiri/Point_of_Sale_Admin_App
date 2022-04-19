package com.denkiri.pharmacy.storage.daos
import androidx.lifecycle.LiveData
import androidx.room.*
import com.denkiri.pharmacy.models.expense.TotalIncome
@Dao
interface TotalIncomeDao {
    @Query("SELECT *FROM TotalIncome LIMIT 1")
    fun getTotalIncome(): LiveData<TotalIncome>
    @Query("SELECT * FROM TotalIncome LIMIT 1")
    fun fetch(): TotalIncome
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTotalIncome(model: TotalIncome)
    @Delete
    fun deleteTotalIncome(model:TotalIncome)
    @Query("DELETE FROM TotalIncome")
    fun delete()
}