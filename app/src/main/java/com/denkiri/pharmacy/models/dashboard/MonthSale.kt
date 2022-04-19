package com.denkiri.pharmacy.models.dashboard
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
@Entity
class MonthSale {
    @PrimaryKey()
    @SerializedName("monthly_sales")
    @Expose
    var monthlySales: Double = 0.0
    @Ignore
    constructor(monthSale: Double) {
        this.monthlySales = monthSale
    }
    constructor()
}