package com.denkiri.pharmacy.models.dashboard
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
@Entity(
        indices = [(Index("monthlyProfit"))],
        primaryKeys = ["monthlyProfit"]
)
class MonthProfit {
    @SerializedName("monthlyProfit")
    @Expose
    var monthlyProfit: Double =0.0
    @Ignore
    constructor(monthProfit: Double) {
        this.monthlyProfit = monthProfit
    }
    constructor()
}