package com.denkiri.pharmacy.storage.daos
import androidx.lifecycle.LiveData
import androidx.room.*
import com.denkiri.pharmacy.models.dashboard.Vat
@Dao
interface VatDao {
    @Query("SELECT *FROM Vat LIMIT 1")
    fun getYearVat(): LiveData<Vat>
    @Query("SELECT * FROM Vat LIMIT 1")
    fun fetch(): Vat
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertYearVat(model: Vat)
    @Delete
    fun deleteYearVat(model: Vat)
    @Query("DELETE FROM Vat")
    fun delete()
}