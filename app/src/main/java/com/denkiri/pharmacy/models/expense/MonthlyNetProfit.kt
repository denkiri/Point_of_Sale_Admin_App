package com.denkiri.pharmacy.models.expense

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
@Entity(
    indices = [(Index("monthlyNetProfit"))],
    primaryKeys = ["monthlyNetProfit"]
)
class MonthlyNetProfit {
    @SerializedName("monthly_net_profit")
    @Expose
    var monthlyNetProfit: Double = 0.0
    @Ignore
    constructor(monthlyNetProfit: Double){
        this.monthlyNetProfit = monthlyNetProfit
    }
    constructor()
}