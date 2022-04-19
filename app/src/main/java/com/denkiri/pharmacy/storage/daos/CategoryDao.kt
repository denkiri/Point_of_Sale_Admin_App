package com.denkiri.pharmacy.storage.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.denkiri.pharmacy.models.category.Category

@Dao
interface CategoryDao {
    @Query(value = "Select * from Category")
    fun getAllCategories() : LiveData<List<Category>>

}