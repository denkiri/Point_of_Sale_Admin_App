package com.denkiri.pharmacy.storage.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.denkiri.pharmacy.models.reports.collectionReport.TotalCollection
@Dao
interface TotalCollectionDao {
    @Query("SELECT *FROM TotalCollection LIMIT 1")
    fun getTotalCollection(): LiveData<TotalCollection>
    @Query("SELECT * FROM TotalCollection LIMIT 1")
    fun fetch(): TotalCollection
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTotalCollection(model: TotalCollection)
    @Delete
    fun deleteTotalCollection(model: TotalCollection)
    @Query("DELETE FROM TotalCollection")
    fun delete()
}