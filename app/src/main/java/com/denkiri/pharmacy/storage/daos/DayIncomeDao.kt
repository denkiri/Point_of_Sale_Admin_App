package com.denkiri.pharmacy.storage.daos
import androidx.lifecycle.LiveData
import androidx.room.*
import com.denkiri.pharmacy.models.expense.DayIncome
@Dao
interface DayIncomeDao {
    @Query("SELECT *FROM DayIncome LIMIT 1")
    fun getDayIncome(): LiveData<DayIncome>
    @Query("SELECT * FROM DayIncome LIMIT 1")
    fun fetch(): DayIncome
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDayIncome(model: DayIncome)
    @Delete
    fun deleteDayIncome(model:DayIncome)
    @Query("DELETE FROM DayIncome")
    fun delete()
}