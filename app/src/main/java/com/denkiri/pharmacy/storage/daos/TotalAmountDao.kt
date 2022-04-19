package com.denkiri.pharmacy.storage.daos
import androidx.lifecycle.LiveData
import androidx.room.*
import com.denkiri.pharmacy.models.order.TotalInvoiceAmount
@Dao
interface TotalAmountDao {
    @Query("SELECT *FROM TotalInvoiceAmount LIMIT 1")
    fun getTotalAmount(): LiveData<TotalInvoiceAmount>
    @Query("SELECT * FROM TotalInvoiceAmount  LIMIT 1")
    fun fetch(): TotalInvoiceAmount
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTotalAmount(model: TotalInvoiceAmount )
    @Delete
    fun deleteTotalAmount(model: TotalInvoiceAmount )
    @Query("DELETE FROM TotalInvoiceAmount ")
    fun delete()
}