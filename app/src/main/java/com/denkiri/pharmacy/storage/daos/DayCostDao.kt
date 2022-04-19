package com.denkiri.pharmacy.storage.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.denkiri.pharmacy.models.dashboard.DayCost
import com.denkiri.pharmacy.models.oauth.Profile
@Dao
interface DayCostDao {
    @Query("SELECT *FROM DayCost LIMIT 1")
    fun getDayCost(): LiveData<DayCost>
    @Query("SELECT * FROM DayCost LIMIT 1")
    fun fetch(): DayCost
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDayCost(model: DayCost)
    @Delete
    fun deleteDayCost(model: DayCost)
    @Query("DELETE FROM DayCost")
    fun delete()
}