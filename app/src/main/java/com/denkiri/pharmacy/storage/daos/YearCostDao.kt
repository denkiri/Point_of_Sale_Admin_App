package com.denkiri.pharmacy.storage.daos
import androidx.lifecycle.LiveData
import androidx.room.*
import com.denkiri.pharmacy.models.dashboard.YearCost
import com.denkiri.pharmacy.models.oauth.Profile
@Dao
interface YearCostDao {
    @Query("SELECT *FROM YearCost LIMIT 1")
    fun getYearCost(): LiveData<YearCost>
    @Query("SELECT * FROM YearCost LIMIT 1")
    fun fetch(): YearCost
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertYearCost(model: YearCost)
    @Delete
    fun deleteYearCost(model: YearCost)
    @Query("DELETE FROM YearCost")
    fun delete()
}