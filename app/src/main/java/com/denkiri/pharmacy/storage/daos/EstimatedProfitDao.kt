package com.denkiri.pharmacy.storage.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.denkiri.pharmacy.models.product.EstimatedProfit
@Dao
interface EstimatedProfitDao {
    @Query("SELECT *FROM EstimatedProfit LIMIT 1")
    fun getEstimatedProfit(): LiveData<EstimatedProfit>
    @Query("SELECT * FROM EstimatedProfit LIMIT 1")
    fun fetch(): EstimatedProfit
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEstimatedProfit(model: EstimatedProfit)
    @Delete
    fun deleteEstimatedProfit(model: EstimatedProfit)
    @Query("DELETE FROM EstimatedProfit")
    fun delete()
}