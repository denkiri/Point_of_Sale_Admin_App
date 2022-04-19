package com.denkiri.pharmacy.storage.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.denkiri.pharmacy.models.product.Items
@Dao
interface ItemsDao {
    @Query("SELECT *FROM Items LIMIT 1")
    fun getItems(): LiveData<Items>
    @Query("SELECT * FROM Items LIMIT 1")
    fun fetch(): Items
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItems(model: Items)
    @Delete
    fun deleteItems(model: Items)
    @Query("DELETE FROM Items")
    fun delete()
}