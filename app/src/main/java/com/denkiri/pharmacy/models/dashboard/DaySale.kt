package com.denkiri.pharmacy.models.dashboard
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
@Entity
class DaySale {
    @PrimaryKey()
    @SerializedName("day_sales")
    @Expose
    var daySales: Double = 0.0
    @Ignore
    constructor(daySales: Double) {
        this.daySales = daySales
    }
    constructor()


}