package com.denkiri.pharmacy.storage.daos
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.denkiri.pharmacy.models.brand.Brand
@Dao
interface BrandDao {
    @Query(value = "Select * from Brand")
    fun getAllBrands() : LiveData<List<Brand>>

}