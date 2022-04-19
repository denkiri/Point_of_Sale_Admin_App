package com.denkiri.pharmacy.storage.daos
import androidx.lifecycle.LiveData
import androidx.room.*
import com.denkiri.pharmacy.models.dashboard.MonthSale
@Dao
interface MonthSaleDao {
    @Query("SELECT *FROM MonthSale LIMIT 1")
    fun getMonthSale(): LiveData<MonthSale>
    @Query("SELECT * FROM MonthSale LIMIT 1")
    fun fetch(): MonthSale
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMonthSale(model: MonthSale)
    @Delete
    fun deleteMonthSale(model: MonthSale)
    @Query("DELETE FROM MonthSale")
    fun delete()
}