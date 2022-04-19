package com.denkiri.pharmacy.storage.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.denkiri.pharmacy.models.dashboard.YearSale
import com.denkiri.pharmacy.models.oauth.Profile
@Dao
interface YearSaleDao {
    @Query("SELECT *FROM YearSale LIMIT 1")
    fun getYearSale(): LiveData<YearSale>
    @Query("SELECT * FROM YearSale LIMIT 1")
    fun fetch(): YearSale
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertYearSale(model: YearSale)
    @Delete
    fun deleteYearSale(model: YearSale)
    @Query("DELETE FROM YearSale")
    fun delete()
}