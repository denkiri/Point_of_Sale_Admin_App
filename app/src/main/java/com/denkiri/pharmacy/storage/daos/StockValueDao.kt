package com.denkiri.pharmacy.storage.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.denkiri.pharmacy.models.product.StockValue
@Dao
interface StockValueDao {
    @Query("SELECT *FROM StockValue LIMIT 1")
    fun getStockValue(): LiveData<StockValue>
    @Query("SELECT * FROM StockValue LIMIT 1")
    fun fetch(): StockValue
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStockValue(model:StockValue)
    @Delete
    fun deleteStockValue(model:StockValue)
    @Query("DELETE FROM StockValue")
    fun delete()
}