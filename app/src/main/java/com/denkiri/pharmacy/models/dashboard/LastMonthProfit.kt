package com.denkiri.pharmacy.models.dashboard

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
@Entity(
        indices = [(Index("lastMonthProfit"))],
        primaryKeys = ["lastMonthProfit"]
)
class LastMonthProfit {
    @SerializedName("lastMonthProfit")
    @Expose
    var lastMonthProfit: Double =0.0
    @Ignore
    constructor(lastMonthProfit: Double) {
        this.lastMonthProfit = lastMonthProfit
    }
    constructor()
}