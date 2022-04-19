package com.denkiri.pharmacy.storage.daos
import androidx.lifecycle.LiveData
import androidx.room.*
import com.denkiri.pharmacy.models.reports.salesReport.TotalSales
@Dao
interface TotalSalesDao {
    @Query("SELECT *FROM TotalSales LIMIT 1")
    fun getTotalSales(): LiveData<TotalSales>
    @Query("SELECT * FROM TotalSales LIMIT 1")
    fun fetch(): TotalSales
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTotalSales(model: TotalSales)
    @Delete
    fun deleteTotalSales(model: TotalSales)
    @Query("DELETE FROM TotalSales")
    fun delete()
}