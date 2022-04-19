package com.denkiri.pharmacy.storage.daos
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.denkiri.pharmacy.models.oauth.Profile

@Dao
interface ProfileDao {
    @Query("SELECT *FROM Profile LIMIT 1")
    fun getProfile():LiveData<Profile>
    @Query("SELECT * FROM Profile LIMIT 1")
    fun fetch():Profile
    @Insert(onConflict = REPLACE)
    fun insertProfile(model:Profile)
    @Update
    fun updateProfile(model: Profile)
    @Delete
    fun deleteProfile(model: Profile)
    @Query("DELETE FROM Profile")
    fun delete()

}