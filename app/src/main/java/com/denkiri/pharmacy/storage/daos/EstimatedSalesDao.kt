package com.denkiri.pharmacy.storage.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.denkiri.pharmacy.models.product.EstimatedSales
@Dao
interface EstimatedSalesDao {
    @Query("SELECT *FROM EstimatedSales LIMIT 1")
    fun getEstimatedSales(): LiveData<EstimatedSales>
    @Query("SELECT * FROM EstimatedSales LIMIT 1")
    fun fetch(): EstimatedSales
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEstimatedSales(model: EstimatedSales)
    @Delete
    fun deleteEstimatedSales(model: EstimatedSales)
    @Query("DELETE FROM EstimatedSales")
    fun delete()
}