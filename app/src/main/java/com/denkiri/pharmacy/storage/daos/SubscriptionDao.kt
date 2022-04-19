package com.denkiri.pharmacy.storage.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.denkiri.pharmacy.models.subscription.Subscription
@Dao
interface SubscriptionDao {
    @Query("SELECT *FROM Subscription LIMIT 1")
    fun getSubscription(): LiveData<Subscription>
    @Query("SELECT * FROM Subscription LIMIT 1")
    fun fetch(): Subscription
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSubscription(model: Subscription)
    @Update
    fun updateSubscription(model: Subscription)
    @Delete
    fun deleteSubscription(model: Subscription)
    @Query("DELETE FROM Subscription")
    fun delete()
}